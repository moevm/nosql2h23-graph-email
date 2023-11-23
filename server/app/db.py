from flask import g
from flask import current_app as app
from neo4j import GraphDatabase


class Neo4jConnection:
    def __init__(self, uri, user, password):
        self.driver = GraphDatabase.driver(uri, auth=(user, password))

    def close(self):
        if self.driver is not None:
            self.driver.close()

    def query(self, query, db=None, **kwargs):
        assert self.driver is not None, "Driver not initialized!"
        session = None
        response = None
        try:
            session = self.driver.session(database=db) if db is not None else self.driver.session()
            response = list(session.run(query, **kwargs))
        except Exception as e:
            print("Query failed:", e)
        finally:
            if session is not None:
                session.close()
        return response


def get_db():
    db = getattr(g, "database", None)
    if db is None:
        uri = app.config.get('NEO4J_URI')
        user = app.config.get('NEO4J_USER')
        password = app.config.get('NEO4J_PASSWORD')
        db = Neo4jConnection(uri, user=user, password=password)
    return db


def close_db():
    db = g.pop('database', None)
    if db is not None:
        db.close()
