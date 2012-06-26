package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route4Test extends CamelSpringTestSupport {
	
	@EndpointInject(uri = "mock:billingUpdate")
	MockEndpoint mockBillingUpdate;

	@Test
	public void testHappyFlow() throws Exception {
		// stop the routes which consume from this route to apply our dummy results
		context.stopRoute("Route5");
		context.stopRoute("Route6");
		
		// add our dummy routes
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// mimic the behavior of 'Route5'
				from("direct:CRM_ADDRESS_UPDATE")
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception {
							exchange.getOut().setHeaders(exchange.getIn().getHeaders());
							exchange.getOut().setBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>DONE</processingState><processingDate>2012-05-21T23:32:19.563</processingDate></addressChange>");
						}
					});
				
				// mimic the behavior of 'Route6'
				from("seda:BILLING_UPDATE")
					.to("mock:billingUpdate");
			}
		});
		
		mockBillingUpdate.expectedMessageCount(1);
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		String responseBody = template.requestBody("activemq:queue:ADDRESS_CHANGE", requestBody, String.class);
		
		assertMockEndpointsSatisfied();
		
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>DONE</processingState><processingDate>2012-05-21T23:32:19.563</processingDate></addressChange>",
				responseBody);
	}

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-part-2.xml"});
    }
}