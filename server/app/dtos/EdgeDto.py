from typing import Dict, Union
import datetime
from neo4j.time import DateTime


class Edge:
    def __init__(self, edge):
        self.identity: int = edge['identity']
        self.start: int = edge['start']
        self.end: int = edge['end']
        self.type: str = edge['type']
        self.properties: Dict[str, str] = edge['properties']
        date = self.properties.get('date', None)
        if date is not None and isinstance(date, str):
            date = self.convert_to_isoformat(date)  # Date.parse(date)  #
        self.date: Union[datetime.date, str] = date
        self.element_id: str = edge['elementId']
        self.start_node_element_id: str = edge['startNodeElementId']
        self.end_node_element_id: str = edge['endNodeElementId']

    def to_dict(self):
        return {
            "date": self.date,
            "source": self.start_node_element_id,
            "target": self.end_node_element_id,
            "type": self.type
        }

    @staticmethod
    def convert_to_isoformat(date_str: str) -> Union[DateTime, str]:
        try:
            # Assuming the input date string is in a known format
            date_obj = DateTime.fromisoformat(date_str)  # datetime.datetime.strptime(date_str, '%Y-%m-%dT%H:%M:%SZ')
            return date_obj
        except ValueError as e:
            # Handle the case where the date string is not in the expected format
            print(f"Error converting date: {e}")
            return date_str
