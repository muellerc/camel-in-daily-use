package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressDTO;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route3Test extends CamelSpringTestSupport {
	
	@Test
	public void processFile() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// provide a mock result which is normally provided in a later processing step
				from("activemq:queue:ADDRESS_CHANGE")
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception {
							exchange.getOut().copyFrom(exchange.getIn());
							exchange.getOut().setBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>DONE</processingState><processingDate>2012-05-21T23:32:19.563</processingDate></addressChange>");
						}
					});
			}
		});
		
		Exchange receivedExchange = template.request("direct:route3", new Processor() {
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody(createAddressChangeDTO());
			}
		});
		
		Object body = receivedExchange.getOut().getBody();
		assertIsInstanceOf(String.class, body);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>DONE</processingState><processingDate>2012-05-21T23:32:19.563</processingDate></addressChange>",
				body);
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

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-rs-test.xml", "META-INF/spring/application-context.xml"});
    }
}