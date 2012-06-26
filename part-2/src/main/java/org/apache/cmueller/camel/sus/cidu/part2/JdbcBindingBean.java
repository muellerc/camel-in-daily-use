package org.apache.cmueller.camel.sus.cidu.part2;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.language.Simple;
import org.springframework.jdbc.object.StoredProcedure;

public class JdbcBindingBean {
	
	private StoredProcedure storedProcedure;
	
	public void process(
			@Simple("body.clientId") String clientId,
			@Simple("body.requestId") String requestId,
			@Simple("body.oldAddress.id") Integer addressIdd) {
		
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("CLIENT_ID", clientId);
		inParams.put("REQUEST_ID", requestId);
		inParams.put("ADDRESS_ID", addressIdd);
		
		storedProcedure.execute(inParams);
	}

	public void setStoredProcedure(StoredProcedure storedProcedure) {
    	this.storedProcedure = storedProcedure;
    }
}