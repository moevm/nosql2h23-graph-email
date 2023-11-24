from typing import List, Dict, Union, Optional
import datetime
import isodate


class Letter:

    def __init__(self, identity: int, elementId: str, labels: List[str], properties: Dict[str, Union[str, int, List[str]]]):
        self._identity: int = identity
        self.element_id: str = elementId
        self.labels: List[str] = labels
        self.properties: Dict[str, Union[str, int, List[str]]] = properties
        self.subject: str = self.properties.get('subject', None)
        self.full_text: str = self.properties.get('full_text', None)
        self.id_chain: int = self.properties.get('id_chain', None)
        self._from: str = self.properties.get('from', None)
        self.to: List[str] = self.properties.get('to', None)
        self.order_in_chain: int = self.properties.get('order_in_chain', None)
        self.time_on_reply: Optional[str, datetime.timedelta] = self.properties.get('time_on_reply', None)
        try:
            if self.time_on_reply and isinstance(self.time_on_reply, str):
                self.time_on_reply: datetime.timedelta = isodate.parse_duration(self.time_on_reply)
        except isodate.isoerror.ISO8601Error as e:
            print(f"Error converting time_on_reply: {e}")

    def to_dict(self):
        person_dict = {
            "id": self.element_id,
            "labels": self.labels,
            "from": self._from,
            "full_text": self.full_text,
            "id_chain": self.id_chain,
            "order_in_chain": self.order_in_chain,
            "subject": self.subject,
            "to": self.to
            }
        if self.time_on_reply:
            person_dict["time_on_reply"] = self.time_on_reply
        return person_dict

    def __repr__(self):
        return "L from {}, to {}".format(self._from, self.to)