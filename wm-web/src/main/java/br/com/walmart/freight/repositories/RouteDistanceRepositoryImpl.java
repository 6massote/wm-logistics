package br.com.walmart.freight.repositories;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("routeDistanceRespository")
public class RouteDistanceRepositoryImpl implements RouteDistanceRepository {

	@Autowired
	private Driver driver;

	@Override
	public boolean createOrUpdateBy(String map, String from, String to, Float distance) {
		// Float.toString keeps 0.1f as 0.1 instead of the widened 0.10000000149...
		final Map<String, Object> params = Map.of(
				"map", map, "from", from, "to", to, "distance", Double.valueOf(distance.toString()));

		try (Session session = driver.session()) {
			return session.executeWrite(tx -> tx
					.run(RouteQueryHelper.ROUTE_DISTANCE_MERGE, params)
					.consume()
					.counters()
					.relationshipsCreated() > 0);
		}
	}

	@Override
	public List<RouteEdge> findAllBy(String map) {
		try (Session session = driver.session()) {
			return session.executeRead(tx -> tx
					.run(RouteQueryHelper.FIND_ROUTES_BY_MAP, Map.<String, Object>of("map", map))
					.list(record -> new RouteEdge(
							record.get("from").asString(),
							record.get("to").asString(),
							record.get("distance").asDouble())));
		}
	}

}
