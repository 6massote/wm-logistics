package br.com.walmart.database.repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.walmart.freight.repositories.RouteQueryHelper;

@Repository("neo4jRepository")
public class Neo4jRepositoryImpl implements Neo4jRepository {

	@Autowired
	private Driver driver;

	@Override
	public void resetNodesAndRelationships() {
		try (Session session = driver.session()) {
			session.executeWrite(tx -> tx.run(RouteQueryHelper.DESTROY_ALL_NODES_AND_RELATIONSHIPS).consume());
		}
	}

	@Override
	public void createIndexes() {
		try (Session session = driver.session()) {
			session.run(RouteQueryHelper.CREATE_LOCATION_INDEX).consume();
		}
	}

}
