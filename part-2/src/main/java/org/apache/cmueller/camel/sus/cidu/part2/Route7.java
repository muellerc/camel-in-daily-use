package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.builder.RouteBuilder;

public class Route7 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toEndpoint;

	@Override
	public void configure() {
		from(fromEndpoint).routeId("Route7")
			.to("bean:sqlBindingBean")
			.to(toEndpoint);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToEndpoint(String toEndpoint) {
    	this.toEndpoint = toEndpoint;
    }
}