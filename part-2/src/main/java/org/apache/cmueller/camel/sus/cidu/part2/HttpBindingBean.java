package org.apache.cmueller.camel.sus.cidu.part2;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class HttpBindingBean {
	
	public AddressChangeDTO updateAddress(Exchange exchange) {
		AddressChangeDTO result = exchange.getIn().getBody(AddressChangeDTO.class);
		result.setProcessingState("DONE");
		result.setProcessingDate(new Date(1340727030283l));
		
		return result;
	}
}