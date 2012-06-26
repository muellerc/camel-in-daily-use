package org.apache.cmueller.camel.sus.cidu.part2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class AuditAddressUpdateStoredProcedureDef {

	public static void audit(String clientId, String requestId, Integer addressId) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:default:connection");
			
	        try {
	            PreparedStatement ps = conn.prepareStatement("INSERT INTO ADDRESS_UPDATES (CLIENT_ID, REQUEST_ID, ADDRESS_ID, UPDATED_AT) VALUES (?, ?, ?, ?)");
	            
	            try {
		            ps.setString(1, clientId);
		            ps.setString(2, requestId);
		            ps.setInt(3, addressId);
		            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	                ps.execute();
                } finally {
                	try {
                		ps.close();
                	} catch (SQLException e) {
                		// noop
                	}
                }
            } finally {
            	try {
            		conn.close();
            	} catch (SQLException e) {
            		// noop
            	}
            }
        } catch (SQLException e) {
	        throw new RuntimeException(e);
        }
	}
}