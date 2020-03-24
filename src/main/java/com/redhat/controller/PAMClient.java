package com.redhat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKey;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.RuleServicesClient;

import com.cepruledemo.Account;
import com.cepruledemo.AddressChangeEvent;
import com.redhat.claim.ClaimEvent;

import fraud.PatientInfo;
import fraud.Prescription;




public class PAMClient {
	
	
	public static void main(String[] s){
		
		PAMClient el = new PAMClient();
		AddressChangeEvent event = new AddressChangeEvent();
    	event.setAccountId("1005");
    	event.setEventDT(new Date());
    	event.setEventId(""+10001);
		try {
			el.startProcess(event, "+16083584153", "jpaulraj@redhat.com");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//el.execute();
		//el.payload();
	        
	}
	public void sendSignal(long id, String event){
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "NotificationProcess";
	        KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	        ProcessServicesClient processServiceClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
	        processServiceClient.signalProcessInstance(containerId, id, "customerResponse", event);
	}
	public long startProcess(AddressChangeEvent event,String phone,String email) throws UnsupportedEncodingException{
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "NotificationProcess";
	        KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	        ProcessServicesClient processServiceClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
	       // Account acount = event.getAccount() ;
	        String accountId = event.getAccountId();
	        String message = URLEncoder.encode("Your address have been changed multiple time with in a few minutes. Please enter Yes to confirm that the changes were made by you or No", "UTF-8");
	        
	        Map param =new HashMap();
	        param.put("accountId", accountId);
	       
	        param.put("email", email);
	        param.put("mphone", phone);
	        param.put("message", message);
	        //CorrelationKeyFactory factory = KieInternalServices.Factory.get().newCorrelationKeyFactory(); 
	        
	       // CorrelationKey key  = factory.newCorrelationKey(accountId);
	        
	        //long pid = processServiceClient.startProcess(containerId, "NotificationProcess.NotificationProcess", key,param);
	        long pid = processServiceClient.startProcess(containerId, "NotificationProcess.NotificationProcess",param);
	       
	        System.out.println("\t######### Process instance" + pid);
	        return pid;
	       
	}
	public long startProcess(Map<String, Object> params) {
		
		
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     //healthcare.ClaimNotification
	       String password = "jboss123$";
	       String containerId = "preauth";
	       KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	       ProcessServicesClient processServiceClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
	       Long id = processServiceClient.startProcess("preauth", "healthcare.ClaimNotification",params);
	       
	       System.out.println("process started with instance id " + id);
		return id.longValue();

	}
	public long startFraudAlertProcess(PatientInfo p) throws UnsupportedEncodingException{
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "FraudRxProcess";
	        KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	        ProcessServicesClient processServiceClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
	        Map param =new HashMap();
	        param.put("patient", p);
	       
	      
	        long pid = processServiceClient.startProcess(containerId, "FraudRxProcess.FraudRxAlertProcess",param);
	       
	        System.out.println("\t######### Process instance" + pid);
	        return pid;
	       
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
	public Prescription  executeRule(Prescription prescription){
		
		String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
	    String user = "rhpamAdmin";
	     
	       String password = "jboss123$";
	       String containerId = "fraudalert";
	       KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	       RuleServicesClient ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
	       
	       KieCommands commandsFactory = KieServices.Factory.get().getCommands();
	       Command<?> insertEmp = commandsFactory.newInsert(prescription, "prescription");
	       Command<?> fireAllRules = commandsFactory.newFireAllRules();
	       Command<?> batchCommand = commandsFactory.newBatchExecution(Arrays.asList(insertEmp,fireAllRules));
	       ServiceResponse<ExecutionResults> results = ruleClient.executeCommandsWithResults(containerId, batchCommand);
	       ExecutionResults result = results.getResult();
	       Prescription prescriptionResult = (Prescription)result.getValue("prescription");
	       return prescriptionResult;
	}
public static KieServicesClient configure(String url, String username, String password) {
		
		//default marshalling format is JAXB
		KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
		
	
		config.setMarshallingFormat(MarshallingFormat.XSTREAM);
		//config.setMarshallingFormat(MarshallingFormat.JSON);
		Set<Class<?>> allClasses = new HashSet<Class<?>>();
		allClasses.add(ClaimEvent.class);
		return KieServicesFactory.newKieServicesClient(config);
		//
	}
public void  detete() {
	
	String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
    String user = "rhpamAdmin";
     
       String password = "jboss123$";
       String containerId = "fraudalert";
       KieServicesClient  kieServicesClient = PAMClient.configure(serverUrl, user, password);
	
}

}
