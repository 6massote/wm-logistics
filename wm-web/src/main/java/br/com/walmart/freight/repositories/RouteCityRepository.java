package br.com.walmart.freight.repositories;

public interface RouteCityRepository {

	/**
	 * @return true when the city did not exist in the map and was created
	 */
	boolean createOrUpdateBy(String map, String name);

}
