package br.com.walmart.freight.services;

import br.com.walmart.freight.models.Logistic;

public interface FindShortestPathService {

	/**
	 * @return the shortest distance between the cities, or a negative value when there is no route
	 */
	Float baseOn(Logistic logistic);

}
