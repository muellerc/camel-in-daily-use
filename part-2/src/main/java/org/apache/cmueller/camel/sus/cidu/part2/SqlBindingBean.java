package org.apache.cmueller.camel.sus.cidu.part2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.language.XPath;

public class SqlBindingBean {
	
	public List<Object> process(
			@XPath("/addressChange/clientId") String clientId,
			@XPath("/addressChange/requestId") String requestId,
			@XPath("/addressChange/oldAddress/id") Integer addressd) {
		List<Object> parameter = new ArrayList<Object>();
		parameter.add(clientId);
		parameter.add(requestId);
		parameter.add(addressd);
		parameter.add(new Timestamp(System.currentTimeMillis()));
		
		return parameter;
	}
}