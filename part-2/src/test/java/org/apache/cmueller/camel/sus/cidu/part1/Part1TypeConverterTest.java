package org.apache.cmueller.camel.sus.cidu.part1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.camel.Converter;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressDTO;
import org.apache.cmueller.camel.sus.cidu.part1.model.Address;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChange;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChangeResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class Part1TypeConverterTest {
	
	@Test
	public void testTypeConverterFileExists() throws IOException {
		String typeConverterClassName = IOUtils.toString(Part1TypeConverter.class.getResource("/META-INF/services/org/apache/camel/TypeConverter"));
		
		assertEquals(Part1TypeConverter.class.getName(), typeConverterClassName);
	}
	
	@Test
	public void testTypeConverterAnnotations() throws SecurityException, NoSuchMethodException {
		assertNotNull(Part1TypeConverter.class.getAnnotation(Converter.class));
		assertNotNull(Part1TypeConverter.class.getMethod("toAddressChangeDTO", String.class).getAnnotation(Converter.class));
		assertNotNull(Part1TypeConverter.class.getMethod("toAddressChangeDTO", AddressChange.class).getAnnotation(Converter.class));
	}
	
	@Test
	public void testToAddressChangeResponseOK() throws ParseException {
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setProcessingState("DONE");
		dto.setProcessingDate(new Date());
		
		AddressChangeResponse response = Part1TypeConverter.toAddressChangeResponse(dto);
		
		assertEquals("OK", response.getReturnCode());
	}

	@Test
	public void testToAddressChangeResponseNOK() throws ParseException {
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setProcessingState("FAILED");
		
		AddressChangeResponse response = Part1TypeConverter.toAddressChangeResponse(dto);
		
		assertEquals("NOK", response.getReturnCode());
	}
	
	@Test
	public void testToAddressChangeDTOFromAddressChange() throws ParseException {
		AddressChangeDTO request = Part1TypeConverter.toAddressChangeDTO(createAddressChange());
		
		assertEquals("1", request.getClientId());
		assertNull(request.getRequestId());
		assertEquals("IN PROCESS", request.getProcessingState());
		assertEquals("0815", request.getCustomerNumber());
		
		AddressDTO address = request.getNewAddress();
		assertEquals("Hahnstr.", address.getStreet());
		assertEquals("25", address.getStreetNumber());
		assertEquals("60528", address.getZip());
		assertEquals("Frankfurt", address.getCity());
		assertEquals("Deutschland", address.getCountry());
	}
	
	@Test
	public void testToAddressChangeDTOFromString() throws ParseException {
		AddressChangeDTO request = Part1TypeConverter.toAddressChangeDTO("1,1,0815,Hahnstr.,25,60528,Frankfurt,Deutschland");
		
		assertEquals("1", request.getClientId());
		assertEquals("1", request.getRequestId());
		assertEquals("IN PROCESS", request.getProcessingState());
		assertEquals("0815", request.getCustomerNumber());
		
		AddressDTO address = request.getNewAddress();
		assertEquals("Hahnstr.", address.getStreet());
		assertEquals("25", address.getStreetNumber());
		assertEquals("60528", address.getZip());
		assertEquals("Frankfurt", address.getCity());
		assertEquals("Deutschland", address.getCountry());
	}
	
	private AddressChange createAddressChange() {
	    AddressChange request = new AddressChange();
		request.setClientId("1");
		request.setCustomerNumber("0815");
		Address address = new Address();
		address.setStreet("Hahnstr.");
		address.setStreetNumber("25");
		address.setZip("60528");
		address.setCity("Frankfurt");
		address.setCountry("Deutschland");
		request.setAddress(address);
	    return request;
    }
}