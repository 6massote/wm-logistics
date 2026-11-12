package br.com.walmart.freight.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.repositories.RouteDistanceRepository;
import br.com.walmart.freight.repositories.RouteEdge;

/**
 * Dijkstra over the routes of one map. Distances are never negative (validated on input),
 * which is what makes Dijkstra correct. Runs in O(E log V) instead of enumerating every path.
 */
@Service("findShortestPathService")
public class FindShortestPathServiceImpl implements FindShortestPathService {

	private static final float NO_ROUTE = -1f;

	private record Visit(String city, double distance) {
	}

	@Autowired
	private RouteDistanceRepository routeDistanceRepository;

	@Override
	public Float baseOn(Logistic logistic) {
		final List<RouteEdge> routes = routeDistanceRepository.findAllBy(logistic.getMap());
		final String from = logistic.getFrom();
		final String to = logistic.getTo();

		if (from.equals(to)) {
			// Nothing to travel, but only when the city really is part of the map.
			return cityExists(routes, from) ? 0f : NO_ROUTE;
		}

		final double distance = shortestDistance(routes, from, to);

		return distance < 0 ? NO_ROUTE : (float) distance;
	}

	private boolean cityExists(List<RouteEdge> routes, String city) {
		return routes.stream().anyMatch(route -> route.from().equals(city) || route.to().equals(city));
	}

	/**
	 * @return the shortest distance, or a negative value when the destination cannot be reached
	 */
	private double shortestDistance(List<RouteEdge> routes, String from, String to) {
		final Map<String, List<RouteEdge>> routesByOrigin = new HashMap<>();
		for (RouteEdge route : routes) {
			routesByOrigin.computeIfAbsent(route.from(), city -> new ArrayList<>()).add(route);
		}

		final Map<String, Double> best = new HashMap<>();
		final PriorityQueue<Visit> queue = new PriorityQueue<>(Comparator.comparingDouble(Visit::distance));
		best.put(from, 0d);
		queue.add(new Visit(from, 0d));

		while (!queue.isEmpty()) {
			final Visit current = queue.poll();

			if (current.city().equals(to)) {
				return current.distance();
			}
			if (current.distance() > best.getOrDefault(current.city(), Double.MAX_VALUE)) {
				continue; // stale queue entry, a shorter way to this city was already processed
			}

			for (RouteEdge route : routesByOrigin.getOrDefault(current.city(), List.of())) {
				final double candidate = current.distance() + route.distance();
				if (candidate < best.getOrDefault(route.to(), Double.MAX_VALUE)) {
					best.put(route.to(), candidate);
					queue.add(new Visit(route.to(), candidate));
				}
			}
		}

		return -1;
	}
}
