package br.com.walmart.freight.repositories;

import java.sql.SQLException;

public interface RouteCityRepository {

	public abstract boolean createOrUpdateBy(String map, String name) throws SQLException;
	
}
