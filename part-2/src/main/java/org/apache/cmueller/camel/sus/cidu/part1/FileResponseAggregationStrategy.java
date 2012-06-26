package org.apache.cmueller.camel.sus.cidu.part1;

import java.text.SimpleDateFormat;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.cmueller.camel.sus.cidu.common.model.AddressChangeDTO;

public class FileResponseAggregationStrategy implements AggregationStrategy {
	
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		AddressChangeDTO dto = newExchange.getIn().getBody(AddressChangeDTO.class);
		String line = new StringBuilder(dto.getClientId())
			.append(",")
			.append(dto.getRequestId())
			.append(",")
			.append(dto.getProcessingState())
			.append(",")
			.append(dto.getProcessingDate() != null ? DATE_FORMAT.format(dto.getProcessingDate()) : "")
			.append("\n")
			.toString();
		
		// this happens only for the first exchange
		if (oldExchange == null) {
			newExchange.getIn().setBody(line);
			return newExchange;
		}
		
		oldExchange.getIn().setBody(oldExchange.getIn().getBody(String.class) + line);
		return oldExchange;
	}
}