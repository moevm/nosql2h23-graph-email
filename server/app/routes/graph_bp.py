import neo4j
from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy
from ..dtos import PersonDto, EdgeDto, LetterDto
import json
from ..utilities.BlankFormatter import BlankFormatter
from ..utilities.utilities import parse_json, records_to_array_dtos, get_graph_nodes_edges, to_Date, custom_serializer, is_it_true

db = LocalProxy(get_db)

graph_bp = Blueprint("graph_bp", __name__, url_prefix="/graph")


@graph_bp.route('/', methods=['GET'])
def get_test():
    return "Testing"


@graph_bp.route('/graph_data', methods=['GET'])
def get_graph_data():
    """
        Returns graph data in JSON format:
        nodes: [Nodes]
        links: [Relationships]
        :return: Graph data with nodes and edges.
    """
    try:

        # sort = request.args.get("sort", "id")
        order = request.args.get("order", "ASC")
        skip = request.args.get("skip", 0, type=int)
        limit = request.args.get("limit", 25, type=int)

        email_sender = request.args.get("email_sender", None, type=str)
        emails_delivers = request.args.getlist("email_deliver")
        subject = request.args.get("subject", None, type=str)
        start_date = request.args.get("start_date", None, type=to_Date)
        end_date = request.args.get("end_date", None, type=to_Date)

        is_export = request.args.get("export", default=True, type=is_it_true)
        only_letters = request.args.get("only_letters", default=False, type=is_it_true)
        # Get User ID from JWT Auth (need to add @jwt_required(optional=True) after route)
        # user_id =
        parameters = {
            "order": order,
            "skip": skip,
            "limit": limit
        }
        included_filter = False
        if email_sender:
            parameters["filter_by_sender"] = "WHERE ((r:SEND OR r:DELIVER) AND m.from = SENDER)"
            included_filter = True
        if subject:
            subject = subject.lower()
            filter_by_subject = "WHERE " if not included_filter else "AND "
            filter_by_subject += "(subject = toLower(m.subject) OR subject = toLower(n.subject))"
            parameters["filter_by_subject"] = filter_by_subject
            included_filter = True
        if start_date:
            filter_by_start_date = "WHERE " if not included_filter else "AND "
            filter_by_start_date += "(date_start <= r.date)"
            parameters["filter_by_start_date"] = filter_by_start_date
            included_filter = True
        if end_date:
            filter_by_end_date = "WHERE " if not included_filter else "AND "
            filter_by_end_date += "(r.date <= date_end)"
            parameters["filter_by_end_date"] = filter_by_end_date
            included_filter = True
        if len(emails_delivers) != 0:
            extra_match_for_delivers = ", (m)-[r2:SEND]-(m2:PERSON)"
            extra_return_for_delivers = ", r2, m2"
            filter_by_delivers = "WHERE " if not included_filter else "AND "
            filter_by_delivers += """ALL(email in list_of_delivers WHERE email in m.to)
                AND (ANY(email in list_of_delivers WHERE n.email = email) OR n:MAIN)"""
            parameters["extra_match_for_delivers"] = extra_match_for_delivers
            parameters["extra_return_for_delivers"] = extra_return_for_delivers
            parameters["filter_by_delivers"] = filter_by_delivers
            included_filter = True

        include_letters = True
        include_persons = not only_letters
        include_rels = not only_letters
        fmt = BlankFormatter()
        cypher_query = fmt.format("""
        WITH $sender AS SENDER,
        $subject AS subject,
        datetime($date_start) AS date_start,
        datetime($date_end) AS date_end,
        $delivers AS list_of_delivers
        MATCH (n:PERSON)-[r]-(m:LETTER){extra_match_for_delivers}
        {filter_by_sender}
        {filter_by_subject}
        {filter_by_start_date}
        {filter_by_end_date}
        {filter_by_delivers}
        RETURN n, r, m{extra_return_for_delivers}
        ORDER BY n.id {order}
        SKIP {skip}
        LIMIT {limit};
        """, **parameters)
        records = db.query(cypher_query,
                           sender=email_sender,
                           subject=subject,
                           date_start=start_date,
                           date_end=end_date,
                           delivers=emails_delivers)
        data = records_to_array_dtos(records)
        if is_export:
            return json.dumps(data, default=custom_serializer)
        else:
            # Process the query result into a format suitable for export
            id_to_node, id_to_edge = parse_json(data)
            graph_data_json = get_graph_nodes_edges(id_to_node,
                                                    id_to_edge,
                                                    include_letters,
                                                    include_persons,
                                                    include_rels)
            return graph_data_json  # jsonify(graph_data_json)
    except Exception as e:
        return jsonify({"error": f"Failed get graph. {str(e)}"}), 500


@graph_bp.route('/load_json', methods=['POST'])
def load_json():
    try:
        json_data = request.get_json()
        id_to_node, id_to_edge = parse_json(json_data)
        for node in id_to_node.values():
            if isinstance(node, PersonDto.Person):
                cypher = """
                CREATE (n:{} {{ name : $name, email : $email }})
                """.format(':'.join(node.labels))
                db.query(query=cypher, name=node.name, email=node.email)
            elif isinstance(node, LetterDto.Letter):
                parameters = {
                    'subject': node.subject,
                    'full_text': node.full_text,
                    'id_chain': node.id_chain,
                    '_from': node._from,
                    'to': node.to,
                    'order_in_chain': node.order_in_chain,
                }
                if node.time_on_reply is not None:
                    parameters = {
                        'subject': node.subject,
                        'full_text': node.full_text,
                        'id_chain': node.id_chain,
                        '_from': node._from,
                        'to': node.to,
                        'order_in_chain': node.order_in_chain,
                        'time_on_reply': node.time_on_reply
                    }
                    cypher = """
                    CREATE (n:{}
                    {{ subject : $subject,
                     full_text : $full_text,
                     id_chain : $id_chain,
                     from : $_from,
                     to : $to,
                     order_in_chain : $order_in_chain,
                     time_on_reply : $time_on_reply}})
                    """.format(':'.join(node.labels))
                else:
                    cypher = """
                    CREATE (n:{}
                    {{ subject : $subject,
                     full_text : $full_text,
                     id_chain : $id_chain,
                     from : $_from,
                     to : $to,
                     order_in_chain : $order_in_chain}})
                    """.format(':'.join(node.labels))

                db.query(query=cypher,
                         db=None,
                         **parameters)
        for relationship_obj in id_to_edge.values():
            if isinstance(relationship_obj, EdgeDto.Edge):
                start_node = id_to_node[relationship_obj.start]
                end_node = id_to_node[relationship_obj.end]
                if 'PERSON' in start_node.labels and 'LETTER' in end_node.labels:
                    cypher = """
                    MATCH (start:{0} {{name: $name, email: $email }}), (end:{1} {{id_chain: $id_chain, order_in_chain: $order_in_chain}})
                    CREATE (start)-[:{2} {{date: datetime($date)}}]->(end)
                    """.format(":".join(start_node.labels),
                               ":".join(end_node.labels),
                               relationship_obj.type)
                    db.query(cypher,
                             name=start_node.name,
                             email=start_node.email,
                             id_chain=end_node.id_chain,
                             order_in_chain=end_node.order_in_chain,
                             date=relationship_obj.date)
                elif 'PERSON' in end_node.labels and 'LETTER' in start_node.labels:
                    cypher = """
                    MATCH (end:{0} {{name: $name, email: $email }}), (start:{1} {{id_chain: $id_chain, order_in_chain: $order_in_chain}})
                    CREATE (start)-[:{2} {{date: datetime($date)}}]->(end)
                    """.format(":".join(end_node.labels),
                               ":".join(start_node.labels),
                               relationship_obj.type)
                    db.query(cypher,
                             name=end_node.name,
                             email=end_node.email,
                             id_chain=start_node.id_chain,
                             order_in_chain=start_node.order_in_chain,
                             date=relationship_obj.date)
                elif 'LETTER' in start_node.labels and 'LETTER' in end_node.labels:
                    cypher = """
                    MATCH (start:{0} {{id_chain: $id_chain_start, order_in_chain: $order_in_chain_start}}), (end:{1} {{id_chain: $id_chain_end, order_in_chain: $order_in_chain_end}})
                    CREATE (start)-[:{2} {{date: datetime($date)}}]->(end)
                    """.format(":".join(start_node.labels),
                               ":".join(end_node.labels),
                               relationship_obj.type)
                    db.query(cypher,
                             id_chain_start=start_node.id_chain,
                             order_in_chain_start=start_node.order_in_chain,
                             id_chain_end=end_node.id_chain,
                             order_in_chain_end=end_node.order_in_chain,
                             date=relationship_obj.date)
        return {"message": "JSON data successfully loaded into Neo4j database"}
    except Exception as e:
        # Handle exceptions (log, return error message, etc.)
        return jsonify({"error": f"Failed to load JSON data. {str(e)}"}), 500