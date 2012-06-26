package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.builder.RouteBuilder;

public class Route6 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toBillingSystem1Endpoint;
	private String toBillingSystem2Endpoint;

	@Override
	public void configure() {
		from(fromEndpoint).routeId("Route6")
			// client with id 1 has a flat rate on address updates ;-)
			.filter().xpath("/addressChange/clientId!='1'")
				.to("direct:route6")
			.end();
		
		from("direct:route6").routeId("Route6a")
			.choice()
				.when().xpath("/addressChange/clientId<1000")
					.to(toBillingSystem1Endpoint)
				.otherwise()
					.to(toBillingSystem2Endpoint)
			.end();
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToBillingSystem1Endpoint(String toBillingSystem1Endpoint) {
    	this.toBillingSystem1Endpoint = toBillingSystem1Endpoint;
    }
	
	public void setToBillingSystem2Endpoint(String toBillingSystem2Endpoint) {
    	this.toBillingSystem2Endpoint = toBillingSystem2Endpoint;
    }
}