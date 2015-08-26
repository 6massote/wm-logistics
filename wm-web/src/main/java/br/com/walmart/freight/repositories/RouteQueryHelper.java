package br.com.walmart.freight.repositories;

public class RouteQueryHelper {

	public static String buildDestroyAllNodesAndRelationships() {
		return "MATCH (n) OPTIONAL MATCH (n)-[r]->() DELETE n, r";
	}
	
	public static String buildRouteCityCreate(String map, String name) {
		return String.format("CREATE (Location%s%s:Location {maps:'%s', name:'%s'})", trimValue(name), trimValue(map), map, name);
	}
	
	public static String buildRouteCityFind(String map, String name) {
		String query = String.format("MATCH (r:Location {maps:'%s', name:'%s'})", map, name);
		query = query.concat(" RETURN count(r)");
		
		return query;
	}
	
	public static String buildRouteDistanceCreate(String map, String from, String to, String distance) {
		String query = String.format("MATCH (from:Location {maps:'%s', name:'%s'}),(to:Location {maps:'%s', name:'%s'})", map, from, map, to);
				
	    query = query.concat(" CREATE");
	    query = query.concat(String.format("(from)-[:CONNECTED_TO { distance: %s }]->(to)", Float.valueOf(distance)));
	    query = query.concat(" RETURN from, to");
	    
	    return query;
	}
	
	public static String buildRouteDistanceUpdate(String map, String from, String to, String distance) {
		String query = String.format("MATCH (from:Location {name:'%s', maps:'%s'})-[r:CONNECTED_TO]->(to:Location {name:'%s', maps:'%s'})", from, map, to, map);
		query = query.concat(String.format(" SET r.distance = %s", Float.valueOf(distance)));
		query = query.concat(" RETURN count(r)");
		
		return query;
	}
	
	public static String buildRouteDistanceFind(String map, String from, String to, String distance) {
		String query = String.format("MATCH (from:Location {maps:'%s', name:'%s'})-[r:CONNECTED_TO]->(to:Location {maps:'%s', name:'%s'})", map, from, map, to);
		query = query.concat("RETURN count(r)");
		
		return query;
	}
	
	public static String buildCalculateShortestPath(String map, String from, String to) {
		String query = String.format("MATCH (from:Location {maps:'%s', name:'%s'}),(to:Location {maps:'%s', name:'%s'}),", map, from, map, to);
		query = query.concat(" path = (from)-[rels:CONNECTED_TO*]->(to)");
		query = query.concat(" RETURN");
		query = query.concat(" reduce(distance=0, r in rels | distance+r.distance) AS totalDistance");
		query = query.concat(" ORDER BY totalDistance ASC");
		query = query.concat(" LIMIT 1");
		
		return query;
	}
	
	private static String trimValue(Object value) {
		return value != null ? value.toString().replace(" ", "") : "";
	}
	
}
