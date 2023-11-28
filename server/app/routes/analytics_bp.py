from neo4j.time import DateTime, Duration
from flask import Blueprint, jsonify, request, send_file, abort
from ..db import get_db, close_db
from werkzeug.local import LocalProxy
from ..dtos import PersonDto, EdgeDto, LetterDto
import json
from ..utilities.BlankFormatter import BlankFormatter
from ..utilities.utilities import to_Date, is_it_true

db = LocalProxy(get_db)

analytics_bp = Blueprint("analytics_bp", __name__, url_prefix="/analytics")


@analytics_bp.route('/get_analytics', methods=['GET'])
def get_analytics():
    try:
        is_iso_format = request.args.get("is_iso_format", False, type=is_it_true)

        email_contact = request.args.get("email_contact", None, type=str)
        id_chain_letter = request.args.get("id_chain", None, type=int)
        start_date = request.args.get("start_date", None, type=to_Date)
        end_date = request.args.get("end_date", None, type=to_Date)

        parameters = {}
        included_filter = False
        if email_contact:
            parameters["filter_email_contact"] = "WHERE (m.from = contact)"
            included_filter = True
        if id_chain_letter:
            filter_by_id_chain = "WHERE " if not included_filter else "AND "
            filter_by_id_chain += "(m.id_chain = id_chain_letter)"
            parameters["filter_by_id_chain"] = filter_by_id_chain
            included_filter = True
        if start_date:
            filter_by_start_date = "WHERE " if not included_filter else "AND "
            filter_by_start_date += "(date_start <= r.date)"
            parameters["filter_by_start_date"] = filter_by_start_date
            included_filter = True
        if end_date:
            filter_by_end_date = "WHERE " if not included_filter else "AND "
            filter_by_end_date += "(r.date <= date_end)"
            parameters["filter_by_end_date"] = filter_by_end_date
            included_filter = True

        fmt = BlankFormatter()
        cypher_query = fmt.format("""
                WITH datetime($date_start) AS date_start,
                datetime($date_end) AS date_end,
                $contact AS contact, 
                $id_chain_letter AS id_chain_letter
                MATCH (n)-[r]->(m:LETTER)
                {filter_email_contact}
                {filter_by_id_chain}
                {filter_by_start_date}
                {filter_by_end_date}
                RETURN max(m.time_on_reply) AS max_time_on_reply,
                avg(m.time_on_reply) AS avg_time_on_reply,
                min(m.time_on_reply) AS min_time_on_reply,
                count(DISTINCT m) AS count_letters; 
                """, **parameters)

        record = db.query(cypher_query,
                           single=True,
                           contact=email_contact,
                           id_chain_letter=id_chain_letter,
                           date_start=start_date,
                           date_end=end_date)

        record_data = {}
        for key, value in record.items():
            if is_iso_format and isinstance(value, Duration):
                value = value.iso_format()
            record_data[key] = value

        return jsonify(record_data)

    except Exception as e:
        return jsonify({"error": f"Failed get analytics. {str(e)}"}), 500


@analytics_bp.route('/get_max_chain', methods=['GET'])
def get_max_chain():
    try:
        pass
    except Exception as e:
        return jsonify({"error": f"Failed get max chain. {str(e)}"}), 500


@analytics_bp.route('/get_min_chain', methods=['GET'])
def get_min_chain():
    try:
        pass
    except Exception as e:
        return jsonify({"error": f"Failed get min chain. {str(e)}"}), 500
