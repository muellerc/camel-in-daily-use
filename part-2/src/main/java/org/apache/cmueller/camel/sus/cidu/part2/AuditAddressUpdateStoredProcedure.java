package org.apache.cmueller.camel.sus.cidu.part2;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class AuditAddressUpdateStoredProcedure extends StoredProcedure {

	public AuditAddressUpdateStoredProcedure(DataSource dataSource, String procedureCall) {
		setDataSource(dataSource);
		setFunction(false);
		setSql(procedureCall);
		
		declareParameter(new SqlParameter("CLIENT_ID", Types.VARCHAR));
		declareParameter(new SqlParameter("REQUEST_ID", Types.VARCHAR));
		declareParameter(new SqlParameter("ADDRESS_ID", Types.INTEGER));
		
		compile();
	}
}