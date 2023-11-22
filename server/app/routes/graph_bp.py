from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy

db = LocalProxy(get_db)

graph_bp = Blueprint('graph_bp', __name__, url_prefix="/graph")


@graph_bp.route('/', methods=['GET'])
def get_test():
    return "Testing"

