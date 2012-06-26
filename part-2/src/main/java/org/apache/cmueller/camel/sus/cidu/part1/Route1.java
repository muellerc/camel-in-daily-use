package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.builder.RouteBuilder;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChangeResponse;

public class Route1 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toEndpoint;

	@Override
	public void configure() {
		from(fromEndpoint).routeId("Route1")
			.convertBodyTo(AddressChangeDTO.class)
			.to(toEndpoint)
			.convertBodyTo(AddressChangeResponse.class);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToEndpoint(String toEndpoint) {
    	this.toEndpoint = toEndpoint;
    }
}