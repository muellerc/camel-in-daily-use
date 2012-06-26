package org.apache.cmueller.camel.sus.cidu.part1;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressDTO;

public class Part1AggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		AddressChangeDTO dto = oldExchange.getIn().getBody(AddressChangeDTO.class);
		Address currentAddress = newExchange.getIn().getBody(Address.class);
		
		AddressDTO oldAddress = new AddressDTO();
		oldAddress.setId(currentAddress.getId());
		oldAddress.setStreet(currentAddress.getStreet());
		oldAddress.setStreetNumber(currentAddress.getStreetNumber());
		oldAddress.setZip(currentAddress.getZip());
		oldAddress.setCity(currentAddress.getCity());
		oldAddress.setCountry(currentAddress.getCountry());
		dto.setOldAddress(oldAddress);
		
		return oldExchange;
	}
}