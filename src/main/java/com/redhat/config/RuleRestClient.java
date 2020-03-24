package com.redhat.config;

import java.util.Calendar;
import java.util.Date;

import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import com.cepruledemo.AddressChangeEvent;
import com.redhat.claim.ClaimEvent;
import com.redhat.model.ClaimSerializer;

public class RuleRestClient{
	
	private KieContainer kieContainer;
	
	private KieSession kieSession;
	
	private KieServices ks;
	
	
	public KieContainer getKieContainer() {
		return kieContainer;
	}
	public void setKieContainer(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}
	public KieSession getKieSession() {
		return kieSession;
	}
	public void setKieSession(KieSession kieSession) {
		this.kieSession = kieSession;
	}
	
	
	public void update(){
		//kieContainer.updateToVersion(version);
	}
	public void executeCEPRule() {
		 KieServices ks = KieServices.Factory.get();
		 ReleaseId rId = ks.newReleaseId("com.cepruledemo", "cepruleDemo", "1.0.5");
		 KieContainer kieContainer = ks.newKieContainer(rId);
		 KieSession	kieSession = kieContainer.newKieSession();
		 
		 Calendar ls = Calendar.getInstance();
			Calendar ls1 = Calendar.getInstance();
			
			ls1.add(Calendar.SECOND, 1);
			
			Date tradeDateTime1 = ls1.getTime();
			Date tradeDateTime = ls.getTime();
			AddressChangeEvent event = new AddressChangeEvent();
	    	event.setAccountId("1001");
	    	event.setEventDT(tradeDateTime);
	    	event.setEventId("1");
	    	//EntryPoint addressEntryPoint = kieSession.getEntryPoint("addressEvent");
	    	 //addressEntryPoint.insert(event);
	    	 
	    	kieSession.insert(event);
	    	int rulefired = kieSession.fireAllRules();
	    	System.out.println("first" + rulefired);
	    	AddressChangeEvent event1 = new AddressChangeEvent();
	    	event1.setAccountId("1001");
	    	event1.setEventDT(tradeDateTime1);
	    	event1.setEventId("2");
	    	// addressEntryPoint.insert(event1);
	    	kieSession.insert(event1);
	    	int rulefire1 = kieSession.fireAllRules();
	    	System.out.println("second" + rulefire1);
	    	
		
	}
	public  KieSession createKieSession(){
		
		ks = KieServices.Factory.get();
    	
    	ReleaseId rId = ks.newReleaseId("redhat", "claimcep", "1.5");
    	
    	kieContainer = ks.newKieContainer(rId);
    	kieSession = kieContainer.newKieSession();
    	System.out.println("session initiated" );
    	kieSession.addEventListener(new AgendaEventListener() {
			
			public void matchCreated(MatchCreatedEvent event) {
				System.out.println("Rule name  ::" + event.getMatch().getRule().getName());
				
			}
			
			
			public void matchCancelled(MatchCancelledEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeMatchFired(BeforeMatchFiredEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void agendaGroupPushed(AgendaGroupPushedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterMatchFired(AfterMatchFiredEvent event) {
				System.out.println("afterMatchFired   ::" + event.getMatch().getRule().getName());
				
			}
		});
    	return kieSession;
	}
	public ClaimEvent executeRule(ClaimEvent event){
		//KieSession kieSession = createKieSession();
		
		
		kieSession.insert(event);
		kieSession.fireAllRules();
		return event;
	}
	
	public ClaimEvent executeRule(){
		KieSession kieSession = createKieSession();
		KieRuntimeLogger logger;
		logger = ks.getLoggers().newFileLogger(kieSession, "/Users/jpaulraj/ruleaudit/ceprule.log");
		kieSession.addEventListener(new AgendaEventListener() {
			
			public void matchCreated(MatchCreatedEvent event) {
				System.out.println("Rule name  ::" + event.getMatch().getRule().getName());
				
			}
			
			
			public void matchCancelled(MatchCancelledEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeMatchFired(BeforeMatchFiredEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void agendaGroupPushed(AgendaGroupPushedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterMatchFired(AfterMatchFiredEvent event) {
				System.out.println("afterMatchFired   ::" + event.getMatch().getRule().getName());
				
			}
		});


		ClaimEvent event = new ClaimEvent();
		Calendar cal = Calendar.getInstance();
		cal.set(2018, 2, 5);
		Date dt1 = cal.getTime();
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2018, 2, 4);
		Date dt2 = cal1.getTime();
		 System.out.println("dt1" + dt1);
		 System.out.println("dt1" + dt2);
		
		event.setClaimId("10021");
		event.setClaimDate(dt1);
		event.setClaimType("surgery");
		event.setMemberName("jey");
		event.setMemmerId("10001");
		event.setAddress("jpaulraj@redhat.com");
		event.setCity("6083584153");
		event.setProviderMPIN("101010");
		event.setClaimAmount(100.0);
		
		ClaimEvent event1 = new ClaimEvent();
		event1.setClaimId("10022");
		event1.setClaimDate(dt2);
		event1.setClaimType("surgery");
		event1.setMemberName("jey");
		event1.setMemmerId("10001");
		event1.setAddress("jpaulraj@redhat.com");
		event1.setCity("6083584153");
		event1.setProviderMPIN("101010");
		event1.setClaimAmount(100.0);
		
		ClaimSerializer ser = new ClaimSerializer();
        String claimevent = ser.serializeClaimEventToJson(event);
        String claimevent1 = ser.serializeClaimEventToJson(event1);
        ClaimEvent events =ser.deserializeJsonToClaimEvent(claimevent);
        ClaimEvent events1 = ser.deserializeJsonToClaimEvent(claimevent1);
        
		kieSession.insert(events);
		kieSession.fireAllRules();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("rule fired 1 initiated" );
		kieSession.insert(events1);
		kieSession.fireAllRules();
		logger.close();
		return event;
	}
	public void deserialize(){
		//RuleExecutor exec = new RuleExecutor();
		ClaimEvent event = new ClaimEvent();
		Calendar cal = Calendar.getInstance();
		cal.set(2018, 2, 5);
		Date dt1 = cal.getTime();
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2018, 2, 3);
		Date dt2 = cal1.getTime();
		 System.out.println("dt1" + dt1);
		 System.out.println("dt1" + dt2);
		
		event.setClaimId("10041");
		event.setClaimDate(dt1);
		event.setClaimType("surgery");
		event.setMemberName("jey");
		event.setMemmerId("10001");
		event.setAddress("jpaulraj@redhat.com");
		event.setCity("6083584153");
		event.setProviderMPIN("101010");
		event.setClaimAmount(100.0);
		
		ClaimEvent event1 = new ClaimEvent();
		event1.setClaimId("10042");
		event1.setClaimDate(dt2);
		event1.setClaimType("surgery");
		event1.setMemberName("jey");
		event1.setMemmerId("10001");
		event1.setAddress("jpaulraj@redhat.com");
		event1.setCity("6083584153");
		event1.setProviderMPIN("101010");
		event1.setClaimAmount(100.0);
		
		ClaimSerializer ser = new ClaimSerializer();
        String claimevent = ser.serializeClaimEventToJson(event);
        System.out.println(claimevent);
       // event.setClaimDate(dt2);
        String claimevent1 = ser.serializeClaimEventToJson(event1);
        System.out.println(claimevent1);
	
	}
	public String executeRule(String event){
		//KieSession kieSession = createKieSession();
		System.out.println("event to be executed " + event.toString());
		ClaimSerializer ser = new ClaimSerializer();
        ClaimEvent claimevent = ser.deserializeJsonToClaimEvent(event);
        System.out.println("deserialisef event to be executed " + event.toString());
        if(kieSession == null){
        	  System.out.println("kie session is null");
        }else{
		kieSession.insert(claimevent);
		kieSession.fireAllRules();
		System.out.println("REsult event" + claimevent.toString());
        }
        String ls = ser.serializeClaimEventToJson(claimevent);
		return ls;
	}
	public static void main(String[] arg){
		RuleExecutor exec = new RuleExecutor();
		//exec.executeRule();
		//exec.deserialize();
		exec.executeCEPRule();
		//exec.deserialize(event);
	}
}
