package br.com.walmart.freight.repositories;

/**
 * Cypher statements. All user input is passed as query parameters ($map, $name, ...),
 * never concatenated into the text, so it cannot change the shape of the query.
 */
public final class RouteQueryHelper {

	public static final String DESTROY_ALL_NODES_AND_RELATIONSHIPS = "MATCH (n) DETACH DELETE n";

	public static final String CREATE_LOCATION_INDEX =
			"CREATE INDEX location_maps_name IF NOT EXISTS FOR (l:Location) ON (l.maps, l.name)";

	public static final String ROUTE_CITY_MERGE =
			"MERGE (l:Location {maps: $map, name: $name})";

	public static final String ROUTE_DISTANCE_MERGE =
			"MATCH (from:Location {maps: $map, name: $from}), (to:Location {maps: $map, name: $to}) "
			+ "MERGE (from)-[r:CONNECTED_TO]->(to) "
			+ "SET r.distance = $distance";

	public static final String CALCULATE_SHORTEST_PATH =
			"MATCH (from:Location {maps: $map, name: $from}), (to:Location {maps: $map, name: $to}), "
			+ "path = (from)-[rels:CONNECTED_TO*]->(to) "
			+ "RETURN reduce(distance = 0.0, r IN rels | distance + r.distance) AS totalDistance "
			+ "ORDER BY totalDistance ASC "
			+ "LIMIT 1";

	private RouteQueryHelper() {
	}

}
