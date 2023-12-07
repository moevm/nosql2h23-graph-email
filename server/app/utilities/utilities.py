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


def path_to_array_dtos(path):
    path_nodes = path.nodes
    path_rels = path.relationships
    id_to_node = {}
    id_to_edge = {}
    # дублирование кода
    for value in path_nodes:
        key = value.id
        if isinstance(value, neo4j.graph.Node):
            labels = list(value.labels)
            properties = dict(value.items())
            if 'PERSON' in labels:
                id_to_node[key] = PersonDto.Person(identity=value.id,
                                                   elementId=value.element_id,
                                                   labels=labels,
                                                   properties=properties)
            elif 'LETTER' in labels:
                if properties.get('time_on_reply', None):
                    properties['time_on_reply'] = properties['time_on_reply'].iso_format()

                id_to_node[key] = LetterDto.Letter(
                    identity=value.id,
                    elementId=value.element_id,
                    labels=labels,
                    properties=properties)

    for value in path_rels:
        if isinstance(value, neo4j.graph.Relationship):
            properties = dict(value.items())
            # TODO: необходимо изменить классы dto, таким образом, чтобы можно было туда закинуть класс Node, Rel и т.п.
            edge = {
                    "identity": value.id,
                    "elementId": value.element_id,
                    "type": value.type,
                    "start": value.start_node.id,
                    "end": value.end_node.id,
                    "startNodeElementId": value.start_node.element_id,
                    "endNodeElementId": value.end_node.element_id,
                    "properties": properties
                }
            relationship = EdgeDto.Edge(edge)
            id_to_edge[relationship.identity] = relationship

    return id_to_node, id_to_edge


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


def records_to_collection_path_count(records):
    chain_ids = {}
    for record in records:
        chain_id = int(record["chain_id"])
        path_count = int(record["path_count"])
        chain_ids[chain_id] = path_count
    return chain_ids


def get_min_chain_id(chain_ids):
    min_chain_id = None
    min_path_count = None
    for chain_id in chain_ids.keys():
        path_count = chain_ids[chain_id]
        if min_path_count is None or path_count < min_path_count:
            min_path_count = path_count
            min_chain_id = chain_id
    return min_chain_id

