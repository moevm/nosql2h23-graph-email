import neo4j
import json
from ..dtos import PersonDto, EdgeDto, LetterDto


def parse_json(json_data):
    def create_node(node_entry):
        node_labels = node_entry['labels']
        node_properties = node_entry['properties']

        if 'PERSON' in node_labels:
            node = PersonDto.Person(identity=node_entry['identity'],
                                    elementId=node_entry['elementId'],
                                    labels=node_labels,
                                    properties=node_properties)

        elif 'LETTER' in node_labels:
            node = LetterDto.Letter(
                identity=node_entry['identity'],
                elementId=node_entry['elementId'],
                labels=node_labels,
                properties=node_properties
            )
        else:
            node = None
        return node

    id_to_node = {}
    id_to_edge = {}
    for entry in json_data:
        for key, value in entry.items():
            if key.startswith('n') or key.startswith('m'):
                node = create_node(value)
                if node._identity not in id_to_node.keys():
                    id_to_node[node._identity] = node
            elif key.startswith('r'):
                relationship = EdgeDto.Edge(value)
                id_to_edge[relationship.identity] = relationship
    return id_to_node, id_to_edge


def records_to_array_dtos(records):
    data = []
    for record in records:
        record_data = {}
        for key, value in record.items():
            if isinstance(value, neo4j.graph.Node):
                properties = dict(value.items())
                if properties.get('time_on_reply', None):
                    properties['time_on_reply'] = properties['time_on_reply'].iso_format()
                record_data[key] = {
                    "labels": list(value.labels),
                    "identity": value.id,
                    "elementId": value.element_id,
                    "properties": properties
                }
            elif isinstance(value, neo4j.graph.Relationship):
                properties = dict(value.items())
                record_data[key] = {
                    "identity": value.id,
                    "elementId": value.element_id,
                    "type": value.type,
                    "start": value.start_node.id,
                    "end": value.end_node.id,
                    "startNodeElementId": value.start_node.element_id,
                    "endNodeElementId": value.end_node.element_id,
                    "properties": properties
                }
            else:
                record_data[key] = value
        data.append(record_data)
    return data


def get_graph_nodes_edges(id_to_node,
                          id_to_edge,
                          include_letters=True,
                          include_persons=True,
                          include_rels=True):
    graph_data = {'nodes_person': [], 'nodes_letter': [], 'links': []}
    for node in id_to_node.values():
        node_dict = node.to_dict()
        if 'PERSON' in node_dict['labels'] and 'MAIN' not in node_dict['labels'] and include_persons:
            graph_data['nodes_person'].append(node_dict)
        elif 'LETTER' in node_dict['labels'] and include_letters:
            graph_data['nodes_letter'].append(node_dict)
        elif 'MAIN' in node_dict['labels'] and include_persons:
            graph_data['main_person'] = node_dict
    if include_rels:
        for relationship_obj in id_to_edge.values():
            graph_data['links'].append(relationship_obj.to_dict())

    graph_data_json = json.dumps(graph_data, default=custom_serializer)
    return graph_data_json


def to_Date(time_str):
    return neo4j.time.DateTime.from_iso_format(time_str)


def custom_serializer(obj):
    if isinstance(obj, neo4j.time.DateTime):
        return str(obj)


def is_it_true(value):
    return value.lower() == 'true'
