package br.com.walmart.freight.repositories;

public interface RouteDistanceRepository {

	/**
	 * @return true when the route did not exist and was created, false when an existing one was updated
	 */
	boolean createOrUpdateBy(String map, String from, String to, Float distance);

	/**
	 * @return the distance of the shortest path, or -1 when there is no path between the cities
	 */
	Float calculateShortestPathBy(String map, String from, String to);

}
