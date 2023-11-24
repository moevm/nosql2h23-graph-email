import datetime
import neo4j
from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy
from ..dtos import PersonDto, EdgeDto, LetterDto
import json

db = LocalProxy(get_db)

graph_bp = Blueprint("graph_bp", __name__, url_prefix="/graph")


def parse_json(json_data):
    id_to_node = {}
    id_to_edge = {}
    for entry in json_data:
        print(entry)
        entry_node = entry['n']
        rel = entry['r']
        target = entry['m']
        if 'PERSON' in entry_node['labels']:
            person_node = PersonDto.Person(identity=entry_node['identity'],
                                           elementId=entry_node['elementId'],
                                           labels=entry_node['labels'],
                                           properties=entry_node['properties'])
            if entry_node['identity'] not in id_to_node.keys():
                id_to_node[entry_node['identity']] = person_node
        elif 'LETTER' in entry_node['labels']:
            letter_node = LetterDto.Letter(identity=entry_node['identity'],
                                           elementId=entry_node['elementId'],
                                           labels=entry_node['labels'],
                                           properties=entry_node['properties'])
            if entry_node['identity'] not in id_to_node.keys():
                id_to_node[entry_node['identity']] = letter_node
        if 'PERSON' in target['labels']:
            person_target = PersonDto.Person(identity=target['identity'],
                                             elementId=target['elementId'],
                                             labels=target['labels'],
                                             properties=target['properties'])
            if target['identity'] not in id_to_node.keys():
                id_to_node[target['identity']] = person_target
        elif 'LETTER' in target['labels']:
            letter_target = LetterDto.Letter(identity=target['identity'],
                                             elementId=target['elementId'],
                                             labels=target['labels'],
                                             properties=target['properties'])
            if target['identity'] not in id_to_node.keys():
                id_to_node[target['identity']] = letter_target
        relationship = EdgeDto.Edge(rel)
        id_to_edge[relationship.identity] = relationship
    return id_to_node, id_to_edge


def custom_serializer(obj):
    # if isinstance(obj, datetime.datetime):
    #     return obj.isoformat()
    # if isinstance(obj, datetime.timedelta):
    #     return str(obj)
    if isinstance(obj, neo4j.time.DateTime):
        return str(obj)


@graph_bp.route('/', methods=['GET'])
def get_test():
    return "Testing"


@graph_bp.route('/get_all', methods=['GET'])
def get_graph():
    """
    Создает граф, затем возвращает его и удаляет его.
    :return: json
    """
    try:
        # Extract pagination values from the request
        sort = request.args.get("sort", "title")
        order = request.args.get("order", "ASC")
        limit = request.args.get("limit", 6, type=int)
        skip = request.args.get("skip", 0, type=int)

        # Get User ID from JWT Auth (need to add @jwt_required(optional=True) after route)
        # user_id =

        cypher_query = """
                        MATCH (n)-[r]->(m)
                        RETURN n, r, m
                        LIMIT 25
                    """
        records = db.query(cypher_query)
        data = []
        for record in records:
            n, r, m = record['n'], record['r'], record['m']
            data.append({
                "n": {
                    "labels": list(n.labels),
                    "identity": n.id,
                    "elementId": n.element_id,
                    "properties": dict(n.items())
                },
                "r": {
                    "identity": r.id,
                    "elementId": r.element_id,
                    "type": r.type,
                    "start": n.id,
                    "end": m.id,
                    "startNodeElementId": n.element_id,
                    "endNodeElementId": m.element_id,
                    "properties": dict(r.items())
                },
                "m": {
                    "labels": list(m.labels),
                    "identity": m.id,
                    "elementId": m.element_id,
                    "properties": dict(m.items())
                }
            })

        # Process the query result into a format suitable for export
        graph_data = {'nodes': [], 'links': []}
        id_to_node, id_to_edge = parse_json(data)
        for node in id_to_node.values():
            graph_data['nodes'].append(node.to_dict())
        for relationship_obj in id_to_edge.values():
            graph_data['links'].append(relationship_obj.to_dict())

        graph_data_json = json.dumps(graph_data, default=custom_serializer)
        return jsonify(graph_data_json)  # jsonify(graph_data_json)
    except Exception as e:
        # Handle exceptions (log, return error message, etc.)
        return jsonify({"error": f"Failed to get all. {str(e)}"}), 500


@graph_bp.route('/load_json', methods=['GET', 'POST'])
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
                    CREATE (start)-[:{2} {{date: $date}}]->(end)
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
                    CREATE (start)-[:{2} {{date: $date}}]->(end)
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
                    CREATE (start)-[:{2} {{date: $date}}]->(end)
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
