package org.apache.cmueller.camel.sus.cidu.part1;

import static org.junit.Assert.*;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressDTO;
import org.junit.Before;
import org.junit.Test;

public class Part1AggregationStrategyTest {
	
	private Part1AggregationStrategy aggregationStrategy;
	private Exchange oldExchange;
	private Exchange newExchange;

	@Before
	public void setUp() {
		aggregationStrategy = new Part1AggregationStrategy();
		
		DefaultCamelContext camelContext = new DefaultCamelContext();
		oldExchange = new DefaultExchange(camelContext);
		newExchange = new DefaultExchange(camelContext);
	}

	@Test
	public void testAggregate() {
		oldExchange.getIn().setBody(createAddressChangeDTO());
		newExchange.getIn().setBody(createAddress());
		
		Exchange exchange = aggregationStrategy.aggregate(oldExchange, newExchange);
		
		assertSame(oldExchange, exchange);
		
		AddressChangeDTO dto = oldExchange.getIn().getBody(AddressChangeDTO.class);
		assertEquals("1", dto.getOldAddress().getId());
		assertEquals("Galileistr.", dto.getOldAddress().getStreet());
		assertEquals("180", dto.getOldAddress().getStreetNumber());
		assertEquals("01129", dto.getOldAddress().getZip());
		assertEquals("Dresden", dto.getOldAddress().getCity());
		assertEquals("Deutschland", dto.getOldAddress().getCountry());
	}

	private AddressChangeDTO createAddressChangeDTO() {
	    AddressChangeDTO dto = new AddressChangeDTO();
	    dto.setClientId("1");
	    dto.setRequestId("1");
	    dto.setCustomerNumber("0815");
	    AddressDTO address = new AddressDTO();
		address.setStreet("Hahnstr.");
		address.setStreetNumber("25");
		address.setZip("60528");
		address.setCity("Frankfurt");
		address.setCountry("Deutschland");
		dto.setNewAddress(address);
	    
	    return dto;
    }

	private Address createAddress() {
		Address address = new Address();
		address.setId("1");
		address.setStreet("Galileistr.");
		address.setStreetNumber("180");
		address.setZip("01129");
		address.setCity("Dresden");
		address.setCountry("Deutschland");
		
	    return address;
    }
}