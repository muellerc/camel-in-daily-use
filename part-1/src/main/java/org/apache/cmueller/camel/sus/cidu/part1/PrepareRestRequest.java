package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class PrepareRestRequest {
	
	public void process(Exchange exchange) {
		AddressChangeDTO dto = exchange.getIn().getBody(AddressChangeDTO.class);
		
		exchange.getIn().setBody(null);
		exchange.getIn().getHeaders().clear();
		
		exchange.getIn().setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.TRUE);
		exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
		exchange.getIn().setHeader(Exchange.ACCEPT_CONTENT_TYPE, "application/json");
		exchange.getIn().setHeader(Exchange.HTTP_PATH, String.format("/customerservice/customers/%s/address", dto.getCustomerNumber()));
		exchange.getIn().setHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS, Address.class);
	}
}