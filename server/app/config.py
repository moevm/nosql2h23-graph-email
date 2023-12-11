from os import environ

FLASK_PORT = environ.get('FLASK_PORT', 5000)
NEO4J_URI = environ.get('NEO4J_URI', "bolt://localhost:7687")
DEBUG = environ.get('DEBUG', 'True') == 'True'
NEO4J_USER = environ.get('NEO4J_USER', "neo4j")
NEO4J_PASSWORD = environ.get('NEO4J_PASSWORD', "admin1234")
URI = environ.get('URI', f"http://192.168.29.94:{FLASK_PORT}")
