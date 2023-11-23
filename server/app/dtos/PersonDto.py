from typing import List, Dict


class Person:

    def __init__(self, identity: int, labels: List[str], properties: Dict[str, str]):
        self._identity: int = identity
        self._labels: List[str] = labels
        self.properties: Dict[str, str] = properties
        self.name: str = self.properties.get('name', None)
        self.email: str = self.properties.get('email', None)

    @property
    def identity(self) -> int:
        return self._identity

    @identity.setter
    def identity(self, identity: int):
        self._identity = identity

    @property
    def labels(self) -> List[str]:
        return self._labels

    @labels.setter
    def labels(self, labels: List[str]):
        self._labels = labels

    def is_main_user(self) -> bool:
        return True if "MAIN" in self._labels else False

