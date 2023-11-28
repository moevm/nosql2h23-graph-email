from flask import Blueprint
from .graph_bp import graph_bp
from .analytics_bp import analytics_bp


api_bp = Blueprint('api', __name__)
api_bp.register_blueprint(graph_bp, url_prefix="/graph")
api_bp.register_blueprint(analytics_bp, url_prefix="/analytics")
