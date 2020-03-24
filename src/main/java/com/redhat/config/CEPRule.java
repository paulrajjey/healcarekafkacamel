package com.redhat.config;

import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
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

public class CEPRule {
	
	private static CEPRule cepRule ;
	
	private KieSession	kieSession;
   
	private CEPRule() {
    	 KieServices ks = KieServices.Factory.get();
		 ReleaseId rId = ks.newReleaseId("com.cepruledemo", "cepruleDemo", "1.0.6");
		 KieContainer kieContainer = ks.newKieContainer(rId);
		 kieSession = kieContainer.newKieSession();
		 KieRuntimeLogger logger;
	    	logger = ks.getLoggers().newConsoleLogger(kieSession);
	    	
	    	kieSession.addEventListener(new AgendaEventListener() {
				
				@Override
				public void matchCreated(MatchCreatedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void matchCancelled(MatchCancelledEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeMatchFired(BeforeMatchFiredEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void agendaGroupPushed(AgendaGroupPushedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void agendaGroupPopped(AgendaGroupPoppedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void afterMatchFired(AfterMatchFiredEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Rule Fired   ::" + event.getMatch().getRule().getName());
					
				}
			});
    }
    public static CEPRule getCEPEngine(){
    	if ( cepRule == null) {
    		System.out.println("null");
    		cepRule = new CEPRule();
    	}else 
    		System.out.println("not null");

    	return  cepRule;
    }
	public KieSession getKieSession() {
		return kieSession;
	}
	
    
}
