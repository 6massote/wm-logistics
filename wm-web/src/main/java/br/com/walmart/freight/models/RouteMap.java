package br.com.walmart.freight.models;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

public class RouteMap implements Serializable {

	private static final long serialVersionUID = -905091615503902448L;
	
	@NotEmpty(message = "error.route_map.name.not_empty")
	private String name;
	@Valid
	@NotEmpty(message = "error.route_map.routes.not_empty")
	private Set<RouteCity> routes;
	
	public RouteMap() {
	}
	
	public RouteMap(String name, Set<RouteCity> routes) {
		this.name = name;
		this.routes = routes;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<RouteCity> getRoutes() {
		return routes;
	}
	public void setRoutes(Set<RouteCity> routes) {
		this.routes = routes;
	}

}
