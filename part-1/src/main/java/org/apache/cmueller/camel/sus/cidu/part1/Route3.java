package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class Route3 extends RouteBuilder {
	
	private String fromEndpoint;
	private String enrichEndpoint;
	private String toEndpoint;
	private AggregationStrategy aggregationStrategy;

	@Override
	public void configure() throws Exception {
		DataFormat jaxb = new JaxbDataFormat(AddressChangeDTO.class.getPackage().getName());
		
		from(fromEndpoint).routeId("Route3")
			.enrich("direct:enrichRoute3", aggregationStrategy)
			.marshal(jaxb)
			.convertBodyTo(String.class)
			.to(toEndpoint);
		
		from("direct:enrichRoute3").routeId("enrichRoute3")
			.to("bean:prepareRestRequest?method=process")
			.inOut(enrichEndpoint);
	}

	public void setFromEndpoint(String fromEndpoint) {
    	this.fromEndpoint = fromEndpoint;
    }

	public void setToEndpoint(String toEndpoint) {
    	this.toEndpoint = toEndpoint;
    }
	
	public void setEnrichEndpoint(String enrichEndpoint) {
    	this.enrichEndpoint = enrichEndpoint;
    }

	public void setAggregationStrategy(AggregationStrategy aggregationStrategy) {
    	this.aggregationStrategy = aggregationStrategy;
    }
}