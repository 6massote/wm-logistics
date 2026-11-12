package br.com.walmart.freight.repositories;

import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("routeCityRespository")
public class RouteCityRepositoryImpl implements RouteCityRepository {

	@Autowired
	private Driver driver;

	@Override
	public boolean createOrUpdateBy(String map, String name) {
		try (Session session = driver.session()) {
			return session.executeWrite(tx -> tx
					.run(RouteQueryHelper.ROUTE_CITY_MERGE, Map.<String, Object>of("map", map, "name", name))
					.consume()
					.counters()
					.nodesCreated() > 0);
		}
	}

}
