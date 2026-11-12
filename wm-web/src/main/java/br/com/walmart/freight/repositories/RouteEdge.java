package br.com.walmart.freight.repositories;

/**
 * One directed route of a map, as stored in the graph.
 */
public record RouteEdge(String from, String to, double distance) {
}
