package org.apache.cmueller.camel.sus.cidu.part1;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.junit.Before;
import org.junit.Test;

public class FileResponseAggregationStrategyTest {
	
	private FileResponseAggregationStrategy aggregationStrategy;
	private Exchange oldExchange;
	private Exchange newExchange;

	@Before
	public void setUp() {
		aggregationStrategy = new FileResponseAggregationStrategy();
		
		CamelContext context = new DefaultCamelContext();
		oldExchange = new DefaultExchange(context);
		newExchange = new DefaultExchange(context);
	}

	@Test
	public void testFirstAggregation() {
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setClientId("1");
		dto.setRequestId("2");
		dto.setProcessingState("DONE");
		dto.setProcessingDate(new Date(0));
		newExchange.getIn().setBody(dto);
		
		Exchange exchange = aggregationStrategy.aggregate(null, newExchange);
		
		assertEquals("1,2,DONE,01.01.1970 01:00:00\n", exchange.getIn().getBody());
	}

	@Test
	public void testSecondOneFurtherAggregations() {
		oldExchange.getIn().setBody("1,2,DONE,01.01.1970 01:00:00\n");
		
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setClientId("3");
		dto.setRequestId("4");
		dto.setProcessingState("FAILED");
		newExchange.getIn().setBody(dto);
		
		Exchange exchange = aggregationStrategy.aggregate(oldExchange, newExchange);
		
		assertEquals("1,2,DONE,01.01.1970 01:00:00\n3,4,FAILED,\n", exchange.getIn().getBody());
	}
}
