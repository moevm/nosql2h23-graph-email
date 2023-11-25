from typing import List, Dict


class Person:

    def __init__(self, identity: int, elementId: str, labels: List[str], properties: Dict[str, str]):
        self._identity: int = identity
        self.labels: List[str] = labels
        self.properties: Dict[str, str] = properties
        self.name: str = self.properties.get('name', None)
        self.email: str = self.properties.get('email', None)
        self.element_id: str = elementId

    def to_dict(self):
        return {
            "id": self.element_id,
            "labels": self.labels,
            "name": self.name,
            "email": self.email
            }

    def __repr__(self):
        return "P: {}, {}".format(self.name, self.email)

    def is_main_user(self) -> bool:
        return True if "MAIN" in self.labels else False

