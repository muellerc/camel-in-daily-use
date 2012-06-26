package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class Route8 extends RouteBuilder {
	
	private String fromEndpoint;

	@Override
	public void configure() {
		JaxbDataFormat jaxb = new JaxbDataFormat(AddressChangeDTO.class.getPackage().getName());
		
		from(fromEndpoint).routeId("Route8")
			.unmarshal(jaxb)
			.to("bean:jdbcBindingBean");
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }
}