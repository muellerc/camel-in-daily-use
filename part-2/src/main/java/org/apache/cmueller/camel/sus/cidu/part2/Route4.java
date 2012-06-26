package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.builder.RouteBuilder;

public class Route4 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toBillingEndpoint;
	private String toCrmEndpoint;

	@Override
	public void configure() {
		from(fromEndpoint).routeId("Route4")
			.multicast()
				.inOnly(toBillingEndpoint)
				.inOut(toCrmEndpoint);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToBillingEndpoint(String toBillingEndpoint) {
    	this.toBillingEndpoint = toBillingEndpoint;
    }
	
	public void setToCrmEndpoint(String toCrmEndpoint) {
    	this.toCrmEndpoint = toCrmEndpoint;
    }
}