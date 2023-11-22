from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy

db = LocalProxy(get_db)

graph_bp = Blueprint("graph_bp", __name__, url_prefix="/graph")


@graph_bp.route('/', methods=['GET'])
def get_test():
    return "Testing"


@graph_bp.route('/get_all', methods=['GET'])
def get_graph():
    """
    Создает граф, затем возвращает его и удаляет его.
    :return: json
    """
    # Extract pagination values from the request
    sort = request.args.get("sort", "title")
    order = request.args.get("order", "ASC")
    limit = request.args.get("limit", 6, type=int)
    skip = request.args.get("skip", 0, type=int)

    # Get User ID from JWT Auth (need to add @jwt_required(optional=True) after route)
    # user_id =

    cypher_query = """
                    MATCH (n)-[r]->(m)
                    RETURN n, labels(n) as node_labels, r, m, labels(m) as target_labels
                    LIMIT 25
                """
    result = db.query(cypher_query)

    # Process the query result into a format suitable for export
    graph_data = {'nodes': [], 'links': []}
    nodes_ids = set()

    for record in result:
        node_data = record['n']
        link_data = record['r']
        target_data = record['m']
        node_labels = record['node_labels']
        target_labels = record['target_labels']

        if "PERSON" in node_labels and (node_data.element_id not in nodes_ids or target_data.element_id not in nodes_ids):
            graph_data['nodes'].append({'id': node_data.element_id,
                                        'labels': node_labels,
                                        'name': node_data.get('name'),
                                        'email': node_data.get('email')
                                        })
            nodes_ids.update([node_data.element_id, target_data.element_id])

        if "LETTER" in target_labels and (node_data.element_id not in nodes_ids or target_data.element_id not in nodes_ids):
            graph_data['nodes'].append({'id': target_data.element_id,
                                        'labels': target_labels,
                                        'from': target_data.get('from'),
                                        'full_text': target_data.get('full_text'),
                                        'id_chain': target_data.get('id_chain'),
                                        'order_in_chain': target_data.get('order_in_chain'),
                                        'subject': target_data.get('subject'),
                                        'time_on_reply': target_data.get('time_on_reply'),
                                        'to': target_data.get('to'),
                                        })
            nodes_ids.update([node_data.element_id, target_data.element_id])

        graph_data['links'].append({'source': node_data.element_id,
                                    'target': target_data.element_id,
                                    'type': link_data.type,
                                    'date': link_data.get('date').isoformat()})

    # Return as JSON
    return jsonify(graph_data)
