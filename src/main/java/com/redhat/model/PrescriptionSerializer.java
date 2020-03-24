package com.redhat.model;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.claim.ClaimEvent;

import fraud.Prescription;

public class PrescriptionSerializer {
	
	
	public byte[] serialize(Prescription prescription) {
	    byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(prescription).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	}
	public String serializeClaimEventToJson(Prescription prescription) {
	    String retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(prescription);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	}
	
	public Prescription deserializeJsonToClaimEvent( String arg1) {
	    ObjectMapper mapper = new ObjectMapper();
	    Prescription prescription = null;
	    try {
	    	prescription = mapper.readValue(arg1, Prescription.class);
	    } catch (Exception e) {

	      e.printStackTrace();
	    }
	    return prescription;
	}

	public static void main(String[] arq){
		ClaimEvent claimEvent = new ClaimEvent();
		claimEvent.setClaimId("1001");
		claimEvent.setProviderMPIN("001011964");
		claimEvent.setClaimDate(new Date());
		PrescriptionSerializer ser = new PrescriptionSerializer();
		//String val = ser.serializeClaimEventToJson(claimEvent);
		
		//System.out.println(val);
		//ClaimEvent claimEven = ser.deserializeJsonToClaimEvent(val);
		///System.out.println(claimEven.toString());

	}
}
