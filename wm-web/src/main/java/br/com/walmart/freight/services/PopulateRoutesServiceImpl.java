package br.com.walmart.freight.services;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.RouteCity;
import br.com.walmart.freight.repositories.RouteCityRepository;
import br.com.walmart.freight.repositories.RouteDistanceRepository;

@Service("populateRoutesService")
public class PopulateRoutesServiceImpl implements PopulateRoutesService {

	@Autowired
	private RouteCityRepository routeCityRespository;
	@Autowired
	private RouteDistanceRepository routeDistanceRespository;
	
	public void inGraph(String map, Collection<RouteCity> routes) throws Exception {
		
		for(Iterator<RouteCity> routeIterator = routes.iterator(); routeIterator.hasNext();) {
			RouteCity routeCity = routeIterator.next();

			// find no graph
			final String from = routeCity.getFrom();
			final String to = routeCity.getTo();
			final String distance = routeCity.getDistance() != null ? routeCity.getDistance().toString() : null;
			
			System.out.println(">>>> Start node and distance creationg for: "+ from +" "+ to +" "+ distance);
			
			final boolean isNewRouteCityFrom = routeCityRespository.createOrUpdateBy(map, from);
			final boolean isNewRouteCityTo = routeCityRespository.createOrUpdateBy(map, to);
			
			final boolean isNewRouteDistance = routeDistanceRespository
					.createOrUpdateBy(map, from, to, distance);
			
			System.out.println(">>>> RouteCity from ["+ from +"] created: " + isNewRouteCityFrom);
			System.out.println(">>>> RouteCity to ["+ to +"] created: " + isNewRouteCityTo);
			System.out.println(">>>> RouteDistance ["+ distance +"] Created: " + isNewRouteDistance);

			
		}		
		
	}

}
