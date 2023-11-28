import neo4j
from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy
from ..dtos import PersonDto, EdgeDto, LetterDto
import json
from ..utilities.BlankFormatter import BlankFormatter

db = LocalProxy(get_db)

analytics_bp = Blueprint("analytics_bp", __name__, url_prefix="/analytics")


@analytics_bp.route('/get_analytics', methods=['GET'])
def get_analytics():
    try:
        pass
    except Exception as e:
        return jsonify({"error": f"Failed get analytics. {str(e)}"}), 500


@analytics_bp.route('/get_max_chain', methods=['GET'])
def get_max_chain():
    try:
        pass
    except Exception as e:
        return jsonify({"error": f"Failed get max chain. {str(e)}"}), 500


@analytics_bp.route('/get_min_chain', methods=['GET'])
def get_max_chain():
    try:
        pass
    except Exception as e:
        return jsonify({"error": f"Failed get min chain. {str(e)}"}), 500

