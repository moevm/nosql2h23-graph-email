from os import environ

FLASK_PORT = environ.get('FLASK_PORT', 5000)
NEO4J_URI = "bolt://localhost:7687"
DEBUG = environ.get('DEBUG', 'True') == 'True'
NEO4J_USER = "admin"
NEO4J_PASSWORD = "admin1234"
