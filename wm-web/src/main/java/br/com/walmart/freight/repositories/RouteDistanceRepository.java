package br.com.walmart.freight.repositories;

import java.sql.SQLException;

public interface RouteDistanceRepository {

	public Boolean createOrUpdateBy(String map, String from, String to, String distance) throws SQLException;
	
	public abstract Float calculateShortestPathBy(String map, String from, String to) throws SQLException;
	
}
