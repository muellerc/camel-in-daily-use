package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route6Test extends CamelSpringTestSupport {
	
	@EndpointInject(uri = "mock:billingSystem1")
	MockEndpoint mockBillingSystem1;

	@EndpointInject(uri = "mock:billingSystem2")
	MockEndpoint mockBillingSystem2;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		// stop the routes which consume from this route to apply our dummy results
		context.stopRoute("Route7");
		context.stopRoute("Route8");
		
		// add our dummy routes to test this route in isolation
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// observe whether 'Route7' is called
				from("direct:CRM_SYSTEM_1_ADDRESS_UPDATE")
					.to("mock:billingSystem1");
				
				// observe whether 'Route8' is called
				from("direct:CRM_SYSTEM_2_ADDRESS_UPDATE")
					.to("mock:billingSystem2");
			}
		});
	}
	
	@Test
	public void testMessageShouldFiltered() throws Exception {
		mockBillingSystem1.expectedMessageCount(0);
		mockBillingSystem1.setMinimumResultWaitTime(3000);
		mockBillingSystem2.expectedMessageCount(0);
		mockBillingSystem2.setMinimumResultWaitTime(3000);
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		template.sendBody("seda:BILLING_AUDITING", requestBody);
		
		assertMockEndpointsSatisfied();
	}
	
	@Test
	public void testMessageShouldRoutedToBillingSystem1() throws Exception {
		mockBillingSystem1.expectedMessageCount(1);
		mockBillingSystem2.expectedMessageCount(0);
		mockBillingSystem2.setMinimumResultWaitTime(3000);
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>99</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		template.sendBody("seda:BILLING_AUDITING", requestBody);
		
		assertMockEndpointsSatisfied();
	}
	
	@Test
	public void testMessageShouldRoutedToBillingSystem2() throws Exception {
		mockBillingSystem1.expectedMessageCount(0);
		mockBillingSystem1.setMinimumResultWaitTime(3000);
		mockBillingSystem2.expectedMessageCount(1);
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1001</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		template.sendBody("seda:BILLING_AUDITING", requestBody);
		
		assertMockEndpointsSatisfied();
	}

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-part-2.xml"});
    }
}
