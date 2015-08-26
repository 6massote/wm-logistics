package br.com.walmart.freight.services;

import java.util.Collection;

import br.com.walmart.freight.models.RouteCity;

public interface PopulateRoutesService {

	public abstract void inGraph(final String map, final Collection<RouteCity> routes) throws Exception;
	
}
