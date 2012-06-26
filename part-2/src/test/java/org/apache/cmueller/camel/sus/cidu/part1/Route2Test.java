package org.apache.cmueller.camel.sus.cidu.part1;

import java.util.Date;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
	
	@EndpointInject(uri = "mock:FileResult")
	MockEndpoint mockFileResult;

	@Test
	public void testHappyCase() throws Exception {
		// the 'mock:result' endpoint should receive three exchange
		mockResult.setExpectedMessageCount(3);
		
		// intercept all calls to 'direct:route3' because we want test Route 2 in isolation (without testing route 3 too)
		// send a copy of the exchange to 'mock:result' so we can easily verify it
		context.getRouteDefinition("Route2")
			.adviceWith(context, new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					interceptSendToEndpoint("direct:route3")
						.skipSendToOriginalEndpoint()
						.to(mockResult)
						.process(new Processor() {
							public void process(Exchange exchange) throws Exception {
								AddressChangeDTO original = exchange.getIn().getBody(AddressChangeDTO.class);
								
								AddressChangeDTO dto = new AddressChangeDTO();
								dto.setClientId(original.getClientId());
								dto.setRequestId(original.getRequestId());
								if ("1".equals(original.getClientId())) {
									dto.setProcessingState("DONE");
									dto.setProcessingDate(new Date(0));									
								} else {
									dto.setProcessingState("FAILED");
								}
								// we do not need the other properties for this test ATM
								
								exchange.getIn().setBody(dto);
							}
						});
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
		
		// check whether all mock endpoint expectations are fulfilled
		assertMockEndpointsSatisfied();
		
		// make sure the file is written, also on slow boxes
		Thread.sleep(2000);
		String fileContent = consumer.receiveBody("file://target/outbox", 5000, String.class);
		assertEquals("1,1,DONE,01.01.1970 01:00:00\n1,2,DONE,01.01.1970 01:00:00\n2,3,FAILED,\n", fileContent);
		
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
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-part-1.xml"});
    }
}