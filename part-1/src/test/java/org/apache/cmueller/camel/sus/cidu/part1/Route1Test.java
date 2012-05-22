package org.apache.cmueller.camel.sus.cidu.part1;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.part1.model.Address;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChange;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChangeResponse;
import org.apache.cmueller.camel.sus.cidu.part1.service.AddressChangeService;
import org.apache.cmueller.camel.sus.cidu.part1.service.AddressChangeService_Service;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route1Test extends CamelSpringTestSupport {
	
	// our test client to call the AddressChangeService provided by Route 1
	private static AddressChangeService client;
	
	@EndpointInject(uri = "mock:result")
	MockEndpoint mockResult;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AddressChangeService_Service service = new AddressChangeService_Service(AddressChangeService_Service.class.getResource("/META-INF/schema/addressChangeService.wsdl"));
		client = service.getAddressChangeServicePort();
		BindingProvider provider = (BindingProvider)client;
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8181/services/AddressChangeService");
	}
	
	@Override
    public boolean isCreateCamelContextPerClass() {
		// we don't want/need a new Spring context and Camel context for each test
        return true;
    }

	@Test
	public void testProcessSuccessful() throws Exception {
		// intercept all calls to 'direct:route3' because we want test Route 1 in isolation (without testing route 3 too)
		// send a copy of the exchange to 'mock:result' so we can easily verify it
		// provide a dummy response because we expect a response in this invocation
		context.getRouteDefinition("Route1")
			.adviceWith(context, new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					interceptSendToEndpoint("direct:route3")
						.skipSendToOriginalEndpoint()
						.to(mockResult)
						.process(new Processor() {
							public void process(Exchange exchange) throws Exception {
								AddressChangeResponse response = new AddressChangeResponse();
								response.setReturnCode("OK");
								
								exchange.getOut().copyFrom(exchange.getIn());
								exchange.getOut().setBody(response);
							}
						});
				}
		});
		
		// the 'mock:result' endpoint should receive one exchange
		mockResult.expectedMessageCount(1);
		
		AddressChangeResponse response = client.changeAddress(createAddressChange());
		
		// check whether all mock endpoint expectations are fulfilled
		assertMockEndpointsSatisfied();
		
		Object request = mockResult.getReceivedExchanges().get(0).getIn().getBody();
		assertIsInstanceOf(AddressChangeDTO.class, request);
		assertEquals("1", ((AddressChangeDTO) request).getClientId());
		assertNull(((AddressChangeDTO) request).getRequestId());
		assertEquals("0815", ((AddressChangeDTO) request).getCustomerNumber());
		assertEquals("IN PROCESS", ((AddressChangeDTO) request).getProcessingState());
		// check more attributes if necessary
		
		// check whether the call was executed successfully
		assertEquals("OK", response.getReturnCode());
	}
	
	@Test
	public void testXmlValidation() throws Exception {
		try {
			// because we don't provide all mandatory field, we expect an exception
	        client.changeAddress(new AddressChange());
	        fail("SOAPFaultException expected");
        } catch (SOAPFaultException e) {
	        assertTrue(e.getMessage().startsWith("Unmarshalling Error: cvc-complex-type.2.4.b: The content of element 'addressChange' is not complete."));
        }
	}
	
	private AddressChange createAddressChange() throws DatatypeConfigurationException {
	    AddressChange request = new AddressChange();
		request.setClientId("1");
		request.setCustomerNumber("0815");
		Address address = new Address();
		address.setStreet("Hahnstr.");
		address.setStreetNumber("25");
		address.setZip("60528");
		address.setCity("Frankfurt");
		address.setCountry("Deutschland");
		request.setAddress(address);
	    return request;
    }

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context.xml"});
    }
}