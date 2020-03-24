package com.redhat.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.drools.core.runtime.impl.ExecutionResultImpl;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.runtime.helper.BatchExecutionHelper;
import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallerFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import com.cepruledemo.AddressChangeEvent;




public class DMClient {
	
	
	public static void main(String[] s){
		
		DMClient el = new DMClient();
		//el.execute();
		//el.payload();
	        
	}
	public void executeRule(AddressChangeEvent event){
		
	}
	public boolean execute(AddressChangeEvent event){
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "cepruleDemo";
	        KieServicesClient  kieServicesClient = DMClient.configure(serverUrl, user, password);

		 RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
	        KieCommands commandsFactory =
	         		KieServices.Factory.get().getCommands();
	       
	       
	       Command<?> insertevent = commandsFactory.newInsert(event, "response");
	       // Command<?> insertevent = commandsFactory.newInsert(event, "response", true, "addressEvent");
	        Command<?> fireAllRules = commandsFactory.newFireAllRules();
	        Command<?> batchCommand =
	      			 commandsFactory.newBatchExecution(Arrays.asList(insertevent,fireAllRules));
	     
	        //Marshaller marshaller = MarshallerFactory.getMarshaller( MarshallingFormat.XSTREAM, getClass().getClassLoader() );
	        //String xStreamXml = marshaller.marshall( batchCommand );
	        //System.out.println("\t######### Rules request"  + xStreamXml);
	   
	       // ServiceResponse<String> result = ruleClient.executeCommands(containerId, batchCommand);
	        ServiceResponse<ExecutionResults> result =  ruleClient.executeCommandsWithResults(containerId, batchCommand);
	     
	       // String resut = result.getResult();
	        ExecutionResults resut = result.getResult();
	        System.out.println("\t######### Rules executed" + result.getMsg());
	        boolean alert = false;
	        Object resultEvent = resut.getValue("response");
	        if(resultEvent!=null) {
	    	  AddressChangeEvent resulte =  (AddressChangeEvent)resultEvent;
	    	  alert = resulte.isAlert();
	    	  System.out.println("EVENT" + resulte.isAlert());
	      }
	       return alert;
	      //System.out.println("\t######### Rules executed" + results.toString());
	       
	}
	/*public void payload(){
		String serverUrl = "http://localhost:8080/kie-execution-server/services/rest/server";
	    String user = "rhdmAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "dm7test";
	       KieServicesClient  kieServicesClient = Mortgage.configure(serverUrl, user, password);
	        RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
	        KieCommands commandsFactory =
	         		KieServices.Factory.get().getCommands();
	        EligibilityCriteria eligiblity = new EligibilityCriteria();
	        eligiblity.setCritrria("BMI");
	        eligiblity.setValue(151);
	        Command<?> insertEmp = commandsFactory.newInsert(eligiblity, "eligibilityResponse");
	        
	        Command<?> fireAllRules = commandsFactory.newFireAllRules();
	        Command<?> batchCommand =
	      			 commandsFactory.newBatchExecution(Arrays.asList(insertEmp,fireAllRules));
	        Marshaller marshaller = MarshallerFactory.getMarshaller( MarshallingFormat.JSON, getClass().getClassLoader() );
	        String result = marshaller.marshall( batchCommand );
	        //String result = BatchExecutionHelper.newXStreamMarshaller().toXML(batchCommand);
	      //  String result = BatchExecutionHelper.newJSonMarshaller().toXML(batchCommand);
	        		//newJSonMarshaller().toXML(batchCommand);

        
	}*/
public static KieServicesClient configure(String url, String username, String password) {
		
		//default marshalling format is JAXB
		KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
		
	
		config.setMarshallingFormat(MarshallingFormat.XSTREAM);
		//config.setMarshallingFormat(MarshallingFormat.JSON);

	
		
		return KieServicesFactory.newKieServicesClient(config);
		//
	}
}
