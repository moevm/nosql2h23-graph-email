from flask import Blueprint
from .graph_bp import graph_bp


api_bp = Blueprint('api', __name__)
api_bp.register_blueprint(graph_bp, url_prefix="/graph")
