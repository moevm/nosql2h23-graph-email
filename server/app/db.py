from flask import g
from flask import current_app as app
from neo4j import GraphDatabase, basic_auth
import time
import logging
import sys

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger(__name__)


class Neo4jConnection:
    def __init__(self, uri, user, password):
        self.driver = GraphDatabase.driver(uri, auth=basic_auth(user, password))
        self.wait_for_db()

    def wait_for_db(self, max_retries=10, delay=2):
        retries = 0
        while retries < max_retries:
            try:
                with self.driver.session() as session:
                    result = session.run("RETURN 1")
                    result.single()
                    print("Connected to Neo4j.")
                    return
            except Exception as e:
                print(f"Error connecting to Neo4j: {e}")
                retries += 1
                time.sleep(delay)
        print("Failed to connect to Neo4j after multiple retries.")

    def close(self):
        if self.driver is not None:
            self.driver.close()

    def query(self, query, single=False, db=None, **kwargs):
        assert self.driver is not None, "Driver not initialized!"
        session = None
        response = None
        try:
            session = self.driver.session(database=db) if db is not None else self.driver.session()
            if single:
                response = session.run(query, **kwargs).single()
            else:
                logger.info("Before response")
                response = list(session.run(query, **kwargs))
                logger.info(response)
                logger.info("Before response")
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
