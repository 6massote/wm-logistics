package br.com.walmart.freight.facades;

import java.sql.SQLException;

import br.com.walmart.core.layers.FacadeContext;
import br.com.walmart.freight.models.RouteMap;

public interface RouteMapFacade {

	public abstract FacadeContext create(final RouteMap routeMap);
	
}
