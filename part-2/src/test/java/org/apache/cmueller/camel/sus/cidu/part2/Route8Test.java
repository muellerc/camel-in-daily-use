package org.apache.cmueller.camel.sus.cidu.part2;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Route8Test extends CamelSpringTestSupport {

	private JdbcTemplate jdbcTemplate;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		jdbcTemplate = new JdbcTemplate(getMandatoryBean(DataSource.class, "dataSource"));
		jdbcTemplate.afterPropertiesSet();
	}

	@Test
	public void testHappyFlow() {
		assertEquals(0, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM ADDRESS_UPDATES"));
		
		String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><addressChange><requestId>44</requestId><clientId>7</clientId><processingState>IN PROCESS</processingState><customerNumber>0815</customerNumber>" +
				"<oldAddress><id>1</id><street>Galileistr.</street><streetNumber>180</streetNumber><zip>01129</zip><city>Dresden</city><country>Deutschland</country></oldAddress>" +
				"<newAddress><street>Hahnstr.</street><streetNumber>25</streetNumber><zip>60528</zip><city>Frankfurt</city><country>Deutschland</country></newAddress></addressChange>";
		template.sendBody("direct:CRM_SYSTEM_2_ADDRESS_UPDATE", requestBody);
		
		assertEquals(1, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM ADDRESS_UPDATES"));
		assertAddressUpdates();
	}
	
	private void assertAddressUpdates() {
		Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM ADDRESS_UPDATES");
	    
		assertEquals("7", row.get("CLIENT_ID"));
		assertEquals("44", row.get("REQUEST_ID"));
		assertEquals(Integer.valueOf(1), row.get("ADDRESS_ID"));
		assertNotNull(row.get("UPDATED_AT"));
    }

	@Override
    protected AbstractApplicationContext createApplicationContext() {
	    return new ClassPathXmlApplicationContext(
	    		new String[]{"META-INF/spring/application-context-test.xml", "META-INF/spring/application-context-part-2.xml"});
    }
}