package org.apache.cmueller.camel.sus.cidu.part1;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Address")
@XmlType(propOrder = { "id", "street", "streetNumber", "zip", "city", "country" })
public class Address {

	private String id;
	private String street;
	private String streetNumber;
	private String zip;
	private String city;
	private String country;
	
	public String getId() {
    	return id;
    }
	
	public void setId(String id) {
    	this.id = id;
    }
	
	public String getStreet() {
    	return street;
    }
	
	public void setStreet(String street) {
    	this.street = street;
    }
	
	public String getStreetNumber() {
    	return streetNumber;
    }
	
	public void setStreetNumber(String streetNumber) {
    	this.streetNumber = streetNumber;
    }
	
	public String getZip() {
    	return zip;
    }
	
	public void setZip(String zip) {
    	this.zip = zip;
    }
	
	public String getCity() {
    	return city;
    }
	
	public void setCity(String city) {
    	this.city = city;
    }
	
	public String getCountry() {
    	return country;
    }
	
	public void setCountry(String country) {
    	this.country = country;
    }
}