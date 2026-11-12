package br.com.walmart.freight.utest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.services.CalculateFreightService;
import br.com.walmart.freight.services.CalculateFreightServiceImpl;

public class CalculateFreightServiceImplTest {

	@Test
	void baseOnValid() {
		final CalculateFreightService service = new CalculateFreightServiceImpl();
		
		final Freight freight = service.baseOn(new Logistic("SP", "A", "D", 10f, 2.5f), 8f);
		
		assertNotNull(freight);
		assertTrue(freight.isValid());
		assertNotNull(freight.getAmount());
		assertEquals(2.0f, freight.getAmount(), 0);
		
	}
	
	@Test
	void baseOnInvalid() {
		final CalculateFreightService service = new CalculateFreightServiceImpl();
		
		final Freight freight = service.baseOn(null, null);
		
		assertNotNull(freight);
		assertFalse(freight.isValid());
		assertNotNull(freight.getAmount());
		assertEquals(-1f, freight.getAmount(), 0);
	}


	@Test
	void baseOnDistanceNotDivisibleByAutonomy() {
		final CalculateFreightService service = new CalculateFreightServiceImpl();

		// 10 / 3 has no exact decimal result: used to throw ArithmeticException.
		final Freight freight = service.baseOn(new Logistic("SP", "A", "D", 3f, 1f), 10f);

		assertEquals(3.3333333f, freight.getAmount(), 0.0001f);
	}

}
