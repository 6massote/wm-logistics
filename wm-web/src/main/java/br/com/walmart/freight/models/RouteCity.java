package br.com.walmart.freight.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class RouteCity {

	@NotEmpty(message = "error.route_city.from.not_empty")
	private String 	from;
	@NotEmpty(message = "error.route_city.to.not_empty")
	private String 	to;
	@NotNull(message = "error.route_city.distance.not_null")
	@DecimalMin(value = "0.00", message = "error.route_city.distance.min_zero")
	private Float 	distance;
	
	public RouteCity() {		
	}
	
	public RouteCity(final String from, final String to, final Float distance) {
		this.from 	= from;
		this.to 	= to;
		this.distance 	= distance;
	}
	
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}
	
}
