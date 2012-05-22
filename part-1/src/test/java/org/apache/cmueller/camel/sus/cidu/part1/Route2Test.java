package org.apache.cmueller.camel.sus.cidu.part1;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route2Test extends CamelSpringTestSupport {
	
	@EndpointInject(uri = "mock:result")
	MockEndpoint mockResult;

	@Test
	public void processFile() throws Exception {
		// intercept all calls to 'direct:route3' because we want test Route 2 in isolation (without testing route 3 too)
		// send a copy of the exchange to 'mock:result' so we can easily verify it
		context.getRouteDefinition("Route2")
			.adviceWith(context, new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					interceptSendToEndpoint("direct:route3")
						.skipSendToOriginalEndpoint()
						.to(mockResult);
				}
		});
		
		// copy our test file into the directory the camel file component observe to start the integration route
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://src/test/data?fileName=address_update_requests.csv&noop=true")
					.to("file://target/inbox");
			}
		});
		
		// the 'mock:result' endpoint should receive three exchange
		mockResult.setExpectedMessageCount(3);
		
		// check whether all mock endpoint expectations are fulfilled
		assertMockEndpointsSatisfied();
		
		List<Exchange> receivedExchanges = mockResult.getReceivedExchanges();
		AddressChangeDTO request1 = (AddressChangeDTO) receivedExchanges.get(0).getIn().getBody();
		assertEquals("1", request1.getRequestId());
		assertEquals("IN PROCESS", request1.getProcessingState());
		// check more attributes if necessary
		
		AddressChangeDTO request2 = (AddressChangeDTO) receivedExchanges.get(1).getIn().getBody();
		assertEquals("2", request2.getRequestId());
		assertEquals("IN PROCESS", request2.getProcessingState());
		// check more attributes if necessary
		
		AddressChangeDTO request3 = (AddressChangeDTO) receivedExchanges.get(2).getIn().getBody();
		assertEquals("3", request3.getRequestId());
		assertEquals("IN PROCESS", request3.getProcessingState());
		// check more attributes if necessary
	}

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context.xml"});
    }
}