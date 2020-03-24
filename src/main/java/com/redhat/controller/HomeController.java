package com.redhat.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redhat.claim.ClaimEvent;
import com.redhat.config.CEPRule;
import com.redhat.model.ClaimSerializer;
import com.redhat.model.PrescriptionSerializer;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.VoiceResponse;

import fraud.Notification;
import fraud.PatientInfo;
import fraud.Prescription;

import com.cepruledemo.Account;
import com.cepruledemo.AddressChangeEvent;

/**
 *
 */
@Controller

public class HomeController {

	@EndpointInject(uri = "direct:kafkaRoute")
	ProducerTemplate kafkaProducer;
	@EndpointInject(uri = "direct:fraudRouter")
	ProducerTemplate fraudProducer;
	
	
	@Autowired
	@Qualifier("ksession")
	private KieSession kieSession;
	
    @RequestMapping("/")
    public String viewHome() {
        return "index";
    }
    @RequestMapping(value = "/incoming_message", method = RequestMethod.POST, produces = "application/xml" )
	public @ResponseBody ResponseEntity<MessagingResponse>  incoming_message(@RequestBody String reqbody ,HttpServletRequest request , HttpServletResponse response) {
		System.out.println("incoming_message" + reqbody);
		HttpSession session = request.getSession(true);
        String counter = (String)session.getAttribute("account");
		System.out.println("session" + counter);
		Enumeration<String>  e = request.getParameterNames();
		String AccountSid = null;
		String MessageSid = null;
		String SmsSid = null;
		String To = null;
		String From = null;
		String NumMedia = null;
		String Body = null;
		VoiceResponse response1 = null;
		MessageSid = request.getParameter("MessageSid");
		AccountSid = request.getParameter("AccountSid");
		SmsSid = request.getParameter("SmsSid");
		From = request.getParameter("From");
		To = request.getParameter("To");
		Body = request.getParameter("Body");
		NumMedia = request.getParameter("NumMedia");
		System.out.println("incoming_message-Body" + Body);
		System.out.println("MessageSid" + MessageSid);
		System.out.println("from" + From);
		System.out.println("MessageSid" + AccountSid);
		System.out.println("MessageSid" + Body);
		String value = "";
		if(Body !=null) {
			if ( Body.toLowerCase().equals("yes")){
				value="Y";
				
			}else if ( Body.toLowerCase().equals("no")){
				value="N";
				
			}
			
		}
		NotificationHandler handler = NotificationHandler.newNotificationHandler();
		System.out.println("phone retrun from " + From);
		if(!From.startsWith("+")) {
			From = "+1"+ From;
			System.out.println("Added +1 " + From);
		}
    	String pid = handler.getMsgNotifications().get(From);
    	
    	System.out.println("phone retrun" + pid);
    	PAMClient pamClient = new PAMClient();
    	long id = Long.parseLong(pid);
    	pamClient.sendSignal(id, value);
    	handler.getMsgNotifications().remove(From);
    	MessagingResponse twiml = new MessagingResponse.Builder()
                .message(new Message.Builder().body(new com.twilio.twiml.Body("your account has been updated")).build())
                .build();
		
		return new ResponseEntity<MessagingResponse> (twiml , HttpStatus.OK);
    }
    
   @GetMapping("/account1")
    public String greeting(@RequestParam( value= "id", required=false, defaultValue="0") String id ) {
       String v ="yes" ;
	   if(id.startsWith("1001"))
        {
		   v="no";
        }
        return v;
    }
  
    @GetMapping("/claim")
    public String greetingForm(Model model) {
    	 ClaimEvent event =  new ClaimEvent();
    	 event.setClaimId("CL1001");
    	 event.setMemmerId("CL1001");
    	 event.setProviderMPIN("001011964");
    	 event.setClaimType("Surgery");
    	 event.setMemberName("Jey");
    	 event.setClaimAmount(100.0);
    	 event.setClaimDate(new Date());
    	 event.setCity("6083584153");
    	 event.setAddress("jpaulraj@redhat.com");

        model.addAttribute("claim", event);
        return "claim";
    }
    @GetMapping("/prescription")
    public String getPrescriptionForm(Model model) {
    	 Prescription event =  new Prescription();
    	 PatientInfo patient = new PatientInfo();
    	 patient.setPatientName("Jey");
    	 patient.setPatientId("1001");
    	 patient.setPatientState("WI");
    	 Notification notification = new Notification();
    	 notification.setPhone("6083584153");
    	 notification.setEmail("jpaulraj@redhat.com");
    	 event.setDoctorName("John");
    	 event.setRxNumber("1001");
    	 event.setPatient(patient);
    	 event.setNotification(notification);
    	 event.setNameofDrug("Opioid");
    	 event.setPharmacyState("WI");
    	

        model.addAttribute("prescription", event);
        return "fraud";
    }
    @GetMapping("/account")
    public String account(Model model) {
    	
    	Account account = new Account();
    	 
    	account.setAccountId("1001");
    	account.setAddress("23030 winter park");
    	account.setCity("Madison");
    	account.setState("WI");
    	account.setDate(new Date());
    	account.setName("Jey");
    	 
        model.addAttribute("account", account);
        return "account";
    }

    @PostMapping("/claimpost")
   // public String greetingSubmit(@RequestParam("claimDate") @DateTimeFormat(pattern="MM/dd/yyyy") Date claimDate, @ModelAttribute(value="claim") ClaimEvent claim) {
    //public String greetingSubmit(@RequestParam("claimDate") String claimDate,@RequestParam("claimDate1") String claimDate1,@ModelAttribute(value="claim") ClaimEvent claim) {
    public String greetingSubmit(@RequestParam("claimDate") String claimDate,@ModelAttribute(value="claim") ClaimEvent claim) {
    	
        Date currentDate = null;
        Date eventDate = null;
		try {
			currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(claimDate);
			//currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(claimDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			eventDate = cal.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		claim.setClaimDate(eventDate);
		if (claim.getAddress() == null || "".equals(claim.getAddress())){
			claim.setAddress("jpaulraj@redhat.com");
		}
		if (claim.getCity() == null || "".equals(claim.getCity())){
			claim.setAddress("6083584153");
		}
		
		 ClaimSerializer ser = new ClaimSerializer();
         String  claimJson = ser.serializeClaimEventToJson(claim);
         kafkaProducer.sendBody("direct:kafkaRoute", claimJson);           	
		System.out.println("test" + claim.toString());
		System.out.println("jason" + claimJson.toString());
        System.out.println("test" + currentDate.toString());
        return "result";
    }
    @PostMapping("/fraud")
     public String Submit(@RequestParam("rxDate") String rxDate,@ModelAttribute(value="prescription") Prescription fraud) {
     	
         Date currentDate = null;
         Date eventDate = null;
 		try {
 			currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(rxDate);
 			//currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(claimDate);
 			Calendar cal = Calendar.getInstance();
 			cal.setTime(currentDate);
 			eventDate = cal.getTime();
 			Random rd = new Random();
 			int rxId = rd.nextInt();
 			fraud.setRxDate(eventDate);
 			fraud.setRxId(rxId+"");
 			System.out.println("Rx" + fraud.toString());
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	/*	claim.setClaimDate(eventDate);
 		if (claim.getAddress() == null || "".equals(claim.getAddress())){
 			claim.setAddress("jpaulraj@redhat.com");
 		}
 		if (claim.getCity() == null || "".equals(claim.getCity())){
 			claim.setAddress("6083584153");
 		}*/
 		fraudProducer.sendBody(fraud);
 		// PrescriptionSerializer ser = new PrescriptionSerializer();
          //String  claimJson = ser.serializeClaimEventToJson(claim);
        //  kafkaProducer.sendBody("direct:kafkaRoute", claimJson);           	
 		//System.out.println("test" + claim.toString());
 		//System.out.println("jason" + claimJson.toString());
         System.out.println("test" + currentDate.toString());
         return "result2";
     }
    @PostMapping("sendnotification")
    public String sendNotitification(
    		@RequestParam("accountId") long accountId,
    		//@RequestParam("processId") String processId,
    		@RequestParam("phone") String phone,
    		@RequestParam("email") String email,@RequestParam("message") String message )
    {
    	String status="";
    	try {
    		//TwilioSMS sms = new TwilioSMS();
    	
    	// status = sms.sendSMS(phone, message);
    	//NotificationHandler handler = NotificationHandler.newNotificationHandler();
    	//System.out.println("processe id from send notifacation--" + processId );
    	//handler.getMsgNotifications().put(phone, processId);
    	SendEmail emailNotification = new SendEmail();
    	List<String> messages = new ArrayList<String>();
    	messages.add(email);
    	messages.add("Address change notitification");
    	messages.add(message);
    	emailNotification.sendEmail(messages);
    	}catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return status;
    	
    }
    @PostMapping("addresspost")
    // public String greetingSubmit(@RequestParam("claimDate") @DateTimeFormat(pattern="MM/dd/yyyy") Date claimDate, @ModelAttribute(value="claim") ClaimEvent claim) {
     //public String greetingSubmit(@RequestParam("claimDate") String claimDate,@RequestParam("claimDate1") String claimDate1,@ModelAttribute(value="claim") ClaimEvent claim) {
     public String Submit(@ModelAttribute(value="account") Account account,@RequestParam("phone") String phone,@RequestParam("email") String email ) throws UnsupportedEncodingException {
     	
    	System.out.println("accoint" + account.getAccountId());
    	System.out.println("accointdate" + account.getDate());
    	System.out.println("accointtime" + account.getDate().getTime());
    	String id = account.getAccountId();
    	Random rd = new Random();
    	int enid = rd.nextInt();
    	AddressChangeEvent event = new AddressChangeEvent();
    	event.setAccountId(id);
    	event.setEventDT(account.getDate());
    	event.setEventId(""+enid);
    	
    	 System.out.println("eventdate" + event.getEventDT());
    	 //EntryPoint addressEntryPoint = kieSession.getEntryPoint("addressEvent");
    	/* KieSession  kieSession = CEPRule.getCEPEngine().getKieSession();
    	 kieSession.insert(event);
    	 int fired = kieSession.fireAllRules();
    	 System.out.println("total size1" + kieSession.getObjects().size());
    	 System.out.println("total size2" + kieSession.getFactHandles().size());
    	 
    	System.out.println("rule fired----" + fired);*/
    	DMClient cl = new DMClient();
    	boolean alert = cl.execute(event);
    	if(alert) {
    		PAMClient pamClient = new PAMClient();
    		long pid = pamClient.startProcess(event,phone,email);
    		NotificationHandler handler = NotificationHandler.newNotificationHandler();
    		NotificationData data = new NotificationData();
    		data.setAccountId(id);
    		data.setEmail(email);
    		data.setPhone(phone);
    		data.setProcessId(pid);
    		handler.getNotifications().put(event.getAccountId(), data);
    		handler.getMsgNotifications().put(phone, ""+pid);
    	}
    	/* Date currentDate = null;
         Date eventDate = null;
 		try {
 			currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(claimDate);
 			//currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(claimDate);
 			Calendar cal = Calendar.getInstance();
 			cal.setTime(currentDate);
 			eventDate = cal.getTime();
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
 		
 		
 		
         return "account";
     }
}
