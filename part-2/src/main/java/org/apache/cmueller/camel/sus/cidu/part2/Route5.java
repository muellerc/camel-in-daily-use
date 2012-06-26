package org.apache.cmueller.camel.sus.cidu.part2;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class Route5 extends RouteBuilder {
	
	private String fromEndpoint;
	private String toEndpoint;

	@Override
	public void configure() {
		JaxbDataFormat jaxb = new JaxbDataFormat(AddressChangeDTO.class.getPackage().getName());
		jaxb.setPrettyPrint(false);
		
		from(fromEndpoint).routeId("Route5")
			.unmarshal(jaxb)
			.setProperty("ORIGINAL_BODY_BACKUP", body())
			.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.PUT))
			.setHeader(Exchange.HTTP_URI, simple(toEndpoint + "/customer/${body.customerNumber}/address/${body.oldAddress.id}"))
			.setBody().xpath("/addressChange/newAddress")
			.convertBodyTo(String.class)
			.to(toEndpoint)
			.setBody(property("ORIGINAL_BODY_BACKUP"))
			.to("bean:httpBindingBean")
			.marshal(jaxb)
			.convertBodyTo(String.class);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }
	
	public void setToEndpoint(String toEndpoint) {
    	this.toEndpoint = toEndpoint;
    }
}