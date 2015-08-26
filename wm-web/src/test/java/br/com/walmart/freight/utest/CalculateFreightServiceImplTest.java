package br.com.walmart.freight.utest;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.walmart.freight.models.Freight;
import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.services.CalculateFreightService;
import br.com.walmart.freight.services.CalculateFreightServiceImpl;

public class CalculateFreightServiceImplTest {

	@Test
	public void baseOnValid() throws Exception {
		final CalculateFreightService service = new CalculateFreightServiceImpl();
		
		final Freight freight = service.baseOn(new Logistic("SP", "A", "D", 10f, 2.5f), 8f);
		
		assertNotNull(freight);
		assertTrue(freight.isValid());
		assertNotNull(freight.getAmount());
		assertEquals(2.0f, freight.getAmount(), 0);
		
	}
	
	@Test
	public void baseOnInvalid() throws Exception {
		final CalculateFreightService service = new CalculateFreightServiceImpl();
		
		final Freight freight = service.baseOn(null, null);
		
		assertNotNull(freight);
		assertFalse(freight.isValid());
		assertNotNull(freight.getAmount());
		assertEquals(-1f, freight.getAmount(), 0);
	}

}
