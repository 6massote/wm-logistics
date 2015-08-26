package br.com.walmart.freight.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import br.com.walmart.core.factories.Neo4jConnectionFactory;

@Service("routeCityRespository")
public class RouteCityRepositoryImpl implements RouteCityRepository {
	
	public boolean createOrUpdateBy(String map, String name) throws SQLException {
		
		final Statement stmt = Neo4jConnectionFactory.getStatement();
		
		ResultSet rs = stmt.executeQuery(RouteQueryHelper.buildRouteCityFind(map, name));
		if (rs.next() && rs.getInt("count(r)") > 0) {
			return false;
		}
		
    	stmt.executeQuery(RouteQueryHelper.buildRouteCityCreate(map, name));
	    
	    return true;
		
	}

}
