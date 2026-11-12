package br.com.walmart.freight.repositories;

import java.util.List;

public interface RouteDistanceRepository {

	/**
	 * @return true when the route did not exist and was created, false when an existing one was updated
	 */
	boolean createOrUpdateBy(String map, String from, String to, Float distance);

	/**
	 * @return every directed route of the map (empty when the map does not exist)
	 */
	List<RouteEdge> findAllBy(String map);

}
