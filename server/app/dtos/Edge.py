from typing import Dict, Optional
import datetime


class Edge:
    def __init__(self, edge):
        self.identity: int = edge['identity']
        self.start: int = edge['start']
        self.end: int = edge['end']
        self.type: str = edge['type']
        self.properties: Dict[str, str] = edge['properties']
        self.date: Optional[datetime.date, str] = self.convert_to_isoformat(self.properties.get('date', None))
        self.element_id: str = edge['elementId']
        self.start_node_element_id: str = edge['startNodeElementId']
        self.end_node_element_id: str = edge['endNodeElementId']

    @staticmethod
    def convert_to_isoformat(date_str: str) -> Optional[datetime.date, str]:
        try:
            # Assuming the input date string is in a known format
            date_obj = datetime.datetime.strptime(date_str, '%Y-%m-%dT%H:%M:%SZ')
            return date_obj
        except ValueError as e:
            # Handle the case where the date string is not in the expected format
            print(f"Error converting date: {e}")
            return date_str
