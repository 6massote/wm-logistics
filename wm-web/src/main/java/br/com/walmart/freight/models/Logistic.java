package br.com.walmart.freight.models;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class Logistic implements Serializable {

	private static final long serialVersionUID = -5635535947341064703L;
	
	@NotEmpty(message = "error.logistic.map.not_empty")
	private String 	map;
	@NotEmpty(message = "error.logistic.from.not_empty")
	private String 	from;
	@NotEmpty(message = "error.logistic.to.not_empty")
	private String 	to;
	@NotNull(message = "error.logistic.autonomy.not_null")
	@DecimalMin(value = "0.00", message = "error.logistic.autonomy.min_value")
	private Float 	autonomy;
	@NotNull(message = "error.logistic.price.not_null")
	@DecimalMin(value = "0.00", message = "error.logistic.price.min_value")
	private Float 	price;
	
	public Logistic() {
	
	}
	
	public Logistic(String map, String from, String to, Float autonomy, Float price) {
		this.map 		= map;
		this.from 		= from;
		this.to 		= to;
		this.autonomy 	= autonomy;
		this.price 		= price;
	}
	
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Float getAutonomy() {
		return autonomy;
	}
	public void setAutonomy(Float autonomy) {
		this.autonomy = autonomy;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
}
