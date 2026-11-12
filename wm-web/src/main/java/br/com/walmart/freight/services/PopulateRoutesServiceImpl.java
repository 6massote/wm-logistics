package br.com.walmart.freight.services;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.RouteCity;
import br.com.walmart.freight.repositories.RouteCityRepository;
import br.com.walmart.freight.repositories.RouteDistanceRepository;

@Service("populateRoutesService")
public class PopulateRoutesServiceImpl implements PopulateRoutesService {

	private static final Logger logger = LoggerFactory.getLogger(PopulateRoutesServiceImpl.class);

	@Autowired
	private RouteCityRepository routeCityRespository;
	@Autowired
	private RouteDistanceRepository routeDistanceRespository;

	@Override
	public void inGraph(String map, Collection<RouteCity> routes) {
		for (RouteCity routeCity : routes) {
			final String from = routeCity.getFrom();
			final String to = routeCity.getTo();

			final boolean isNewFrom = routeCityRespository.createOrUpdateBy(map, from);
			final boolean isNewTo = routeCityRespository.createOrUpdateBy(map, to);
			final boolean isNewDistance = routeDistanceRespository.createOrUpdateBy(map, from, to, routeCity.getDistance());

			logger.debug("Map [{}] {} (new: {}) -> {} (new: {}), distance {} (new: {})",
					map, from, isNewFrom, to, isNewTo, routeCity.getDistance(), isNewDistance);
		}
	}

}
