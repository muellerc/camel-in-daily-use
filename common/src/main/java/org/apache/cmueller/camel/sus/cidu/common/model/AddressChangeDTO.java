package org.apache.cmueller.camel.sus.cidu.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
	    "requestId", "clientId", "processingState", "processingDate", "customerNumber", "oldAddress", "newAddress"
	})
@XmlRootElement(name = "addressChange")
public class AddressChangeDTO implements Serializable {
	
    private static final long serialVersionUID = 3430028006507411952L;
    
    private String clientId;
	private String requestId;
	private String processingState;
	private Date processingDate;
	private String customerNumber;
	private AddressDTO oldAddress;
	private AddressDTO newAddress;
	
	public String getRequestId() {
    	return requestId;
    }
	
	public void setRequestId(String requestId) {
    	this.requestId = requestId;
    }
	
	public String getClientId() {
    	return clientId;
    }
	
	public void setClientId(String clientId) {
    	this.clientId = clientId;
    }
	
	public String getCustomerNumber() {
    	return customerNumber;
    }
	
	public void setCustomerNumber(String customerNumber) {
    	this.customerNumber = customerNumber;
    }

	public AddressDTO getOldAddress() {
    	return oldAddress;
    }

	public void setOldAddress(AddressDTO oldAddress) {
    	this.oldAddress = oldAddress;
    }
	
	public AddressDTO getNewAddress() {
    	return newAddress;
    }

	public void setNewAddress(AddressDTO newAddress) {
    	this.newAddress = newAddress;
    }

	public String getProcessingState() {
    	return processingState;
    }

	public void setProcessingState(String processingState) {
    	this.processingState = processingState;
    }

	public Date getProcessingDate() {
    	return processingDate;
    }

	public void setProcessingDate(Date processingDate) {
    	this.processingDate = processingDate;
    }
}