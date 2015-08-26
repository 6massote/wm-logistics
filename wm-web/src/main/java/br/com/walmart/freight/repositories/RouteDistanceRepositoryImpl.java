package br.com.walmart.freight.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import br.com.walmart.core.factories.Neo4jConnectionFactory;

@Service("routeDistanceRespository")
public class RouteDistanceRepositoryImpl implements RouteDistanceRepository {

	@Override
	public Boolean createOrUpdateBy(String map, String from, String to, String distance) throws SQLException {
		
		final Statement stmt = Neo4jConnectionFactory.getStatement();
		
		ResultSet rs = stmt.executeQuery(RouteQueryHelper.buildRouteDistanceFind(map, from, to, distance));
		if (rs.next() && rs.getInt("count(r)") > 0) {
			stmt.executeQuery(RouteQueryHelper.buildRouteDistanceUpdate(map, from, to, distance));
			return false;
		}
		
		stmt.executeQuery(RouteQueryHelper.buildRouteDistanceCreate(map, from, to, distance));
	    
	    return true;
	    
	}
	
	@Override
	public Float calculateShortestPathBy(String map, String from, String to) throws SQLException {
		
		final Statement stmt = Neo4jConnectionFactory.getStatement();
		
		ResultSet rs = stmt.executeQuery(RouteQueryHelper.buildCalculateShortestPath(map, from, to));
		if (rs.next() && rs.getFloat("totalDistance") >= 0f) {
			return rs.getFloat("totalDistance");
		} 
		
		return -1f;
		
	}

}
