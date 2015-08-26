package br.com.walmart.database.repository;

import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import br.com.walmart.core.factories.Neo4jConnectionFactory;
import br.com.walmart.freight.repositories.RouteQueryHelper;

@Service("neo4jRepository")
public class Neo4jRepositoryImpl implements Neo4jRepository {
	
	public void resetNodesAndRelationships() throws SQLException {
		
		final Statement stmt = Neo4jConnectionFactory.getStatement();
		stmt.executeQuery(RouteQueryHelper.buildDestroyAllNodesAndRelationships());		
		
	}

}
