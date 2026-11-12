package br.com.walmart.freight.services;

import java.math.BigDecimal;
import java.math.MathContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;

@Service("calculateFreightService")
public class CalculateFreightServiceImpl implements CalculateFreightService {

	private static final Logger logger = LoggerFactory.getLogger(CalculateFreightServiceImpl.class);
	private static final float INVALID_FREIGHT = -1f;

	@Override
	public Freight baseOn(final Logistic logistic, Float shortestPathWeight) {
		if (logistic == null || shortestPathWeight == null) {
			return new Freight(INVALID_FREIGHT);
		}

		// Built from the decimal text so 2.5f stays 2.5 and not its binary expansion.
		final BigDecimal autonomy = new BigDecimal(logistic.getAutonomy().toString());
		final BigDecimal price = new BigDecimal(logistic.getPrice().toString());
		final BigDecimal weight = new BigDecimal(shortestPathWeight.toString());

		// A MathContext is required: 10 / 3 has no exact decimal result and divide() would throw.
		final BigDecimal fuelNeeded = weight.divide(autonomy, MathContext.DECIMAL64);
		final BigDecimal consumption = fuelNeeded.multiply(price);

		logger.debug("Freight calculated: {}", consumption);

		return new Freight(consumption.floatValue());
	}
}
