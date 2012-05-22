package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.builder.RouteBuilder;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class Route2 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toEndpoint;

	@Override
	public void configure() throws Exception {
		from(fromEndpoint).routeId("Route2")
			.split(body().tokenize("\n"))
			.convertBodyTo(AddressChangeDTO.class)
			.to(toEndpoint);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToEndpoint(String toEndpoint) {
    	this.toEndpoint = toEndpoint;
    }
}