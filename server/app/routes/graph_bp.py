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
                    RETURN n, r, m
                    LIMIT 25
                """
    result = db.query(cypher_query)

    # Process the query result into a format suitable for export
    graph_data = {'nodes': [], 'links': []}

    for record in result:
        node_data = record['n']
        link_data = record['r']
        target_data = record['m']

        graph_data['nodes'].append({'id': node_data.id,
                                    'label': node_data.type,
                                    'name': node_data.get('name')})
        graph_data['nodes'].append({'id': target_data.id, 'label': target_data.get('name')})
        graph_data['links'].append({'source': node_data.id,
                                    'target': target_data.id,
                                    'type': link_data.type})

    # Return as JSON
    return jsonify(graph_data)
