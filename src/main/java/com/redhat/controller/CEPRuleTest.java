package com.redhat.controller;

import java.util.Calendar;
import java.util.Date;

//import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import com.cepruledemo.AddressChangeEvent;

//import junit.framework.Assert;


public class CEPRuleTest {
	
	

	public KieContainer createContainer() {
    	System.out.println("kie container creation...");
    	
    	KieServices ks = KieServices.Factory.get();
    	
    	ReleaseId rId = ks.newReleaseId("com.cepruledemo", "cepruleDemo", "1.0.2");
    	KieContainer kContainer = ks.newKieContainer(rId);
    	//KieScanner scanner =  ks.newKieScanner(kContainer);
    	//scanner.start(60000);
    	return kContainer;
    }
	
	public static void main(String[] a)
	{
		CEPRuleTest test = new CEPRuleTest();
		
		KieContainer kcontainer = test.createContainer();
		final KieSession kieSession = kcontainer.newKieSession();
		AddressChangeEvent chgEvent = new AddressChangeEvent();
		chgEvent.setAccountId("1001");
		chgEvent.setEventId("1");
		
		AddressChangeEvent chgEvent1 = new AddressChangeEvent();
		chgEvent1.setAccountId("1001");
		chgEvent1.setEventId("2");

		
		Calendar ls = Calendar.getInstance();
		Calendar ls1 = Calendar.getInstance();
		
		ls1.add(Calendar.SECOND, 1);
		
		Date tradeDateTime1 = ls1.getTime();
		Date tradeDateTime = ls.getTime();
		chgEvent.setEventDT(tradeDateTime);
		EntryPoint addressEntryPoint = kieSession.getEntryPoint("addressEvent");
		addressEntryPoint.insert(chgEvent);
		//kieSession.insert(chgEvent);
		
		int no = kieSession.fireAllRules();
		System.out.println(no);
		chgEvent1.setEventDT(tradeDateTime1);
		// kieSession.insert(chgEvent1);
		addressEntryPoint.insert(chgEvent1);
		 
		 int no1 = kieSession.fireAllRules();
		System.out.println(no1);
	}
	//@Test
	public void test() {
		/*KieContainer kcontainer = createContainer();
		final KieSession kieSession = kcontainer.newKieSession();
		LoanApplication lApp = new LoanApplication();
        
        lApp.setAmount(132000);
        lApp.setLengthYears(30);
        lApp.setDeposit(18000);
        
        IncomeSource iSourece = new IncomeSource();
        iSourece.setType("Asset");
        
        Applicant applicant = new Applicant();
        applicant.setAge(35);
        
    	
    	
    	kieSession.insert(lApp);
    	kieSession.insert(iSourece);
    	kieSession.insert(applicant);
    
    	int rulesFired = kieSession.fireAllRules();
    	System.out.println(rulesFired);
    	Assert.assertEquals( 3, rulesFired );*/
		
	}
}
