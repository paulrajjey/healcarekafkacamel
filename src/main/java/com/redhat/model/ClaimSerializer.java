package com.redhat.model;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.claim.ClaimEvent;

public class ClaimSerializer {
	
	
	public byte[] serialize(ClaimEvent claimEvent) {
	    byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(claimEvent).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	}
	public String serializeClaimEventToJson(ClaimEvent claimEvent) {
	    String retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(claimEvent);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	}
	
	public ClaimEvent deserializeJsonToClaimEvent( String arg1) {
	    ObjectMapper mapper = new ObjectMapper();
	    ClaimEvent claimEvent = null;
	    try {
	    	claimEvent = mapper.readValue(arg1, ClaimEvent.class);
	    } catch (Exception e) {

	      e.printStackTrace();
	    }
	    return claimEvent;
	}

	public static void main(String[] arq){
		ClaimEvent claimEvent = new ClaimEvent();
		claimEvent.setClaimId("1001");
		claimEvent.setProviderMPIN("001011964");
		claimEvent.setClaimDate(new Date());
		ClaimSerializer ser = new ClaimSerializer();
		String val = ser.serializeClaimEventToJson(claimEvent);
		
		System.out.println(val);
		ClaimEvent claimEven = ser.deserializeJsonToClaimEvent(val);
		System.out.println(claimEven.toString());

	}
}
