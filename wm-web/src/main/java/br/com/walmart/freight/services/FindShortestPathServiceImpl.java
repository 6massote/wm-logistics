package br.com.walmart.freight.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.repositories.RouteDistanceRepository;

@Service("findShortestPathService")
public class FindShortestPathServiceImpl implements FindShortestPathService {

	@Autowired
	private RouteDistanceRepository routeDistanceRepository;

	@Override
	public Float baseOn(Logistic logistic) {
		return routeDistanceRepository.calculateShortestPathBy(logistic.getMap(), logistic.getFrom(), logistic.getTo());
	}
}
