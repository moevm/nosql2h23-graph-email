from app import app
import logging
import sys

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger(__name__)


if __name__ == "__main__":
    port = app.config.get('FLASK_PORT')
    logger.info("This is an info message.")

    if app.config.get('DEBUG'):
        app.run(host='0.0.0.0', port=port)
    else:
        app.run()
