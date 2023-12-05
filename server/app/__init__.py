from flask import Flask
from flask_cors import CORS
from .routes import api_bp

app = Flask(__name__, template_folder='../template')

app.config.from_pyfile('config.py')
app.register_blueprint(api_bp, url_prefix="/api")
CORS(app)
