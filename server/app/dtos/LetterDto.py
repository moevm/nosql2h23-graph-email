from typing import List, Dict, Union, Optional
import datetime
import isodate


class Letter:

    def __init__(self, identity: int, labels: List[str], properties: Dict[str, Union[str, int, List[str]]]):
        self._identity: int = identity
        self._labels: List[str] = labels
        self.properties: Dict[str, Union[str, int, List[str]]] = properties
        self.subject: str = self.properties.get('subject', None)
        self.full_text: str = self.properties.get('full_text', None)
        self.id_chain: int = self.properties.get('id_chain', None)
        self._from: str = self.properties.get('from', None)
        self.to: List[str] = self.properties.get('to', None)
        self.order_in_chain: int = self.properties.get('order_in_chain', None)
        self.elementId: int = self.properties.get('elementId', None)
        self.time_on_reply: Optional[str] = self.properties.get('time_on_reply', None)
        if self.time_on_reply:
            self.time_on_reply: datetime.timedelta = isodate.parse_duration(self.time_on_reply)
