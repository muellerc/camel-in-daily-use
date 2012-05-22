package org.apache.cmueller.camel.sus.cidu.part1;

import java.util.Scanner;

import org.apache.camel.Converter;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressDTO;
import org.apache.cmueller.camel.sus.cidu.part1.model.Address;
import org.apache.cmueller.camel.sus.cidu.part1.model.AddressChange;

@Converter
public class Part1TypeConverter {
	
	@Converter
	public static AddressChangeDTO toAddressChangeDTO(AddressChange addrChange) {
		Address address = addrChange.getAddress();
		
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setClientId(addrChange.getClientId());
		dto.setProcessingState("IN PROCESS");
		dto.setCustomerNumber(addrChange.getCustomerNumber());
		
		AddressDTO newAddress = new AddressDTO();
		newAddress.setStreet(address.getStreet());
		newAddress.setStreetNumber(address.getStreetNumber());
		newAddress.setZip(address.getZip());
		newAddress.setCity(address.getCity());
		newAddress.setCountry(address.getCountry());
		dto.setNewAddress(newAddress);
		
		return dto;
	}
	
	@Converter
	public static AddressChangeDTO toAddressChangeDTO(String line) {
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(",");
		
		AddressChangeDTO dto = new AddressChangeDTO();
		dto.setClientId(scanner.next());
		dto.setRequestId(scanner.next());
		dto.setProcessingState("IN PROCESS");
		dto.setCustomerNumber(scanner.next());
		
		AddressDTO newAddress = new AddressDTO();
		newAddress.setStreet(scanner.next());
		newAddress.setStreetNumber(scanner.next());
		newAddress.setZip(scanner.next());
		newAddress.setCity(scanner.next());
		newAddress.setCountry(scanner.next());
		dto.setNewAddress(newAddress);
		
		return dto;
	}
}