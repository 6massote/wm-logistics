package br.com.walmart.freight.services;

import java.util.Collection;

import br.com.walmart.freight.models.RouteCity;

public interface PopulateRoutesService {

	void inGraph(String map, Collection<RouteCity> routes);

}
