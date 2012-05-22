package org.apache.cmueller.camel.sus.cidu.part1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.junit.Before;
import org.junit.Test;

public class PrepareHttpRequestTest {
	
	private PrepareRestRequest processor;
	private Exchange exchange;

	@Before
	public void setUp() {
		processor = new PrepareRestRequest();
		
		exchange = new DefaultExchange(new DefaultCamelContext());
	}

	@Test
	public void testProcess() throws DatatypeConfigurationException {
		exchange.getIn().setBody(createAddressChangeDTO());
		exchange.getIn().setHeader("dummy", "dummy");
		
		processor.process(exchange);
		
		assertNull(exchange.getIn().getBody());
		assertEquals(5, exchange.getIn().getHeaders().size());
		assertEquals(Boolean.TRUE, exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API));
		assertEquals("GET", exchange.getIn().getHeader(Exchange.HTTP_METHOD));
		assertEquals("application/json", exchange.getIn().getHeader(Exchange.ACCEPT_CONTENT_TYPE));
		assertEquals("/customerservice/customers/0815/address", exchange.getIn().getHeader(Exchange.HTTP_PATH));
		assertEquals(Address.class, exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS));
	}
	
	private AddressChangeDTO createAddressChangeDTO() throws DatatypeConfigurationException {
	    AddressChangeDTO dto = new AddressChangeDTO();
	    dto.setCustomerNumber("0815");
	    return dto;
    }
}