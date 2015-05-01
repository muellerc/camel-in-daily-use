package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Route5Test extends CamelSpringTestSupport {
	
	@Test
	public void testHappyFlow() throws Exception {
		context.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("jetty:http://0.0.0.0:8181/services/CmsService/?matchOnUriPrefix=true").routeId("CmsService")
					.process(new Processor() {
						public void process(Exchange exchange) throws Exception {
							//exchange.getIn().getBody(String.class);
							// noop
							// we only expect an HTTP status code 200 if everything works well
							exchange.getOut().setBody("");
						}
					});
			}
		});
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		String responseBody = (String) template.requestBody("direct:CRM_ADDRESS_UPDATE", requestBody);
		
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><addressChange><requestId>1</requestId><clientId>1</clientId><processingState>DONE</processingState><processingDate>2012-06-26T18:10:30.283+02:00</processingDate>" +
				"<customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>",
				responseBody);
	}

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-part-2.xml"});
    }
}