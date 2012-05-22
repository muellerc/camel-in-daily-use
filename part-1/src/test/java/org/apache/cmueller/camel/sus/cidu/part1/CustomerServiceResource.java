package org.apache.cmueller.camel.sus.cidu.part1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/customerservice/")
public class CustomerServiceResource {
	
	@GET
	@Path("/customers/{id}/address")
	@Produces("application/json")
	public Address getCustomer(@PathParam("id") String id) {
		Address address = new Address();
		address.setStreet("Galileistr.");
		address.setStreetNumber("180");
		address.setZip("01129");
		address.setCity("Dresden");
		address.setCountry("Deutschland");
		
		return address;
	}
}
