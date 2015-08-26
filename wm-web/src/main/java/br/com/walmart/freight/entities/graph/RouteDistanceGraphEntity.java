package br.com.walmart.freight.entities.graph;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonProperty;

@RelationshipEntity(type = "DISTANCE_TO")
public class RouteDistanceGraphEntity {
	
	@JsonProperty("distance")
	private String distance;
	@StartNode
	private RouteCityGraphEntity from;
	@EndNode
	private RouteCityGraphEntity to;
	
	public RouteDistanceGraphEntity(RouteCityGraphEntity from, RouteCityGraphEntity to, String distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public RouteCityGraphEntity getFrom() {
		return from;
	}

	public void setFrom(RouteCityGraphEntity from) {
		this.from = from;
	}

	public RouteCityGraphEntity getTo() {
		return to;
	}

	public void setTo(RouteCityGraphEntity to) {
		this.to = to;
	}
	
}
