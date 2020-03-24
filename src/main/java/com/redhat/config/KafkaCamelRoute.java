package com.redhat.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.kie.api.KieBase;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.redhat.claim.ClaimEvent;
import com.redhat.claim.Notification;
import com.redhat.controller.BPMRestClientService;
import com.redhat.controller.PAMClient;
import com.redhat.controller.SendEmail;
import com.redhat.controller.TwilioSMS;
import com.redhat.model.ClaimSerializer;

import fraud.Prescription;

@Configuration
public class KafkaCamelRoute {
	
	
	@Bean(name = "prescriptionFraudRuleExecutor")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PrescriptionFraudRuleExecutor RuleExecutor() {
		
		PrescriptionFraudRuleExecutor ruleExecutor = new PrescriptionFraudRuleExecutor();
		ruleExecutor.createKieSession();
		return ruleExecutor;
		
    }
	@Bean(name = "prescriptionFraudRuleRest")
	public PAMClient RuleRestClient() {
		
		PAMClient pamClient = new PAMClient();
		
		return pamClient;
		
    }
	@Bean(name = "ruleExecuter")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)

	public RuleRestClient RuleClient() {
		
		RuleRestClient ruleExecuter = new RuleRestClient();
		ruleExecuter.createKieSession();
		return ruleExecuter;
		
    }
	
	
	@Bean("ksession")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	
	public KieSession kieSession() throws IOException {
		KieServices ks = KieServices.Factory.get();
    	
    	ReleaseId rId = ks.newReleaseId("com.cepruledemo", "cepruleDemo", "1.0.5");
    	
    	KieContainer kieContainer = ks.newKieContainer(rId);
    
    	KieSession ksesion = kieContainer.newKieSession();

    	KieRuntimeLogger logger;
    	logger = ks.getLoggers().newConsoleLogger(ksesion);
    	
    	ksesion.addEventListener(new AgendaEventListener() {
			
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
	    return ksesion;
	}

	@Bean(name = "KafkaRouteProducer")
	public RouteBuilder kafkaRouteProducer() {
		return new KafkaRouteProducer(
				//"kafka:localhost:9092?topic=test&groupId=testing&autoOffsetReset=earliest&consumersCount=1");
				"kafka:localhost:9092?topic=claimin&groupId=claim&autoOffsetReset=earliest&consumersCount=1");
	}
	
	@Bean(name = "FraudCamelRoute")
	public RouteBuilder FraudCamelRoute() {
		return new FraudCamelRoute(
				"kafka:localhost:9092?topic=claimin&groupId=claim&autoOffsetReset=earliest&consumersCount=1");
	}
	
	@Bean(name = "FraudRouteConsumer")
	public RouteBuilder FraudRouteConsumer() {
		return new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				//from("direct:fraudRouter").to("bean:prescriptionFraudRuleExecutor?method=executeRule(${body})").log("${body}").process(new Processor() {
				from("direct:fraudRouter").to("bean:prescriptionFraudRuleRest?method=executeRule(${body})").log("${body}").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						 Message message = exchange.getIn();
						 Prescription data = (Prescription)message.getBody();
						 boolean isalert = data.isAlert();
						if(isalert) {
							System.out.println("Notification being trigger"  );
							fraud.Notification notification = data.getNotification();
							notification.setMessage("Rx for the patient " + data.getPatient().getPatientName() + " has been issued multiple time in short period");
							TwilioSMS twmsg = new TwilioSMS();
							twmsg.sendSMSMessage(notification.getPhone(), notification.getMessage());
							SendEmail eMail = new SendEmail();
							List<String> messages = new ArrayList<String>();
							messages.add(notification.getEmail());
							messages.add(notification.getMessage());
							messages.add("Prescription fraud alert : Patient " +  data.getPatient().getPatientName());
							eMail.sendEmail(messages);
							PAMClient pclient = new PAMClient();
							pclient.startFraudAlertProcess(data.getPatient());
						}
					}
					
				});

					
			}
		};
	}
	@Bean(name = "KafkaRouteConsumer")
	public RouteBuilder kafkaRouteConsumer() {
		return new RouteBuilder() {
			public void configure() {
				from("kafka:localhost:9092?topic=claimin&groupId=claim&autoOffsetReset=earliest&consumersCount=1")
				.to("bean:ruleExecuter?method=executeRule(${body})").log("${body}").process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						String messageKey = "";
                        if (exchange.getIn() != null) {
                            Message message = exchange.getIn();
                            Integer partitionId = (Integer) message
                                    .getHeader(KafkaConstants.PARTITION);
                            String topicName = (String) message
                                    .getHeader(KafkaConstants.TOPIC);
                            if (message.getHeader(KafkaConstants.KEY) != null)
                                messageKey = (String) message
                                        .getHeader(KafkaConstants.KEY);
                            
                            String data = (String)message.getBody();
                            ClaimSerializer ser = new ClaimSerializer();
                            ClaimEvent claimevent = ser.deserializeJsonToClaimEvent(data);
                            
                            Notification notification = claimevent.getNotificaiton();
                            if(claimevent.getNotificaiton() != null ){
                            	ProducerTemplate template = exchange.getContext().createProducerTemplate();
                            	template.sendBody("kafka:localhost:9092?topic=notification&groupId=claim&autoOffsetReset=earliest&consumersCount=1",data);
                            	String nMessge = notification.getBody();
                            	String nSubject = notification.getSubject();
                            	String nClaimDetail = notification.getClaimInfo();
                            	String nto = claimevent.getAddress();//notification.getTo();
                            	String nsms = claimevent.getCity();
                            	System.out.println("nMessge" + nMessge);
                            	System.out.println("nSubject" + nSubject);
                            	System.out.println("nClaimDetail" + nClaimDetail);
                            	System.out.println("nto" + nto);
                            	
                            	System.out.println("cl" + claimevent.getClaimId());
                            	System.out.println("cl" + claimevent.getMemberName() );
                            	System.out.println("cl" + claimevent.getMemmerId() );
                            	System.out.println("cl" + claimevent.getProviderMPIN() );
                            	

                            	// BPMRestClientService bpmClient = null;
                            	 PAMClient bpmClient = null;
                                try {
                     				 bpmClient = new PAMClient();
                     				 Map<String, Object> params = new HashMap<String, Object>() ;
                     				 params.put("claimId", claimevent.getClaimId());
                     				 params.put("memberName",claimevent.getMemberName() );
                     				 params.put("memberId", claimevent.getMemmerId());
                     				 params.put("ClaimDetail",nClaimDetail);
                     				 params.put("providerMPIN",claimevent.getProviderMPIN());
                     				 
                     				params.put("emailto",nto);
                     				params.put("sms",nsms);
                     				params.put("ebody",nMessge);
                     				params.put("esubject",nSubject);
                     				
                     				 //params.put("notificationMessage", messages);
                     				 
                     				long processid = bpmClient.startProcess(params);
                     				System.out.println("Notification process has been started" +  processid);
                     				
                     			} catch (Exception e) {
                     				// TODO Auto-generated catch block
                     				e.printStackTrace();
                     			}
                            }
                           
                        }
					}
				});		
				//.bean(KafkaOutputBean.class, "doWork");
			}
		};
	}


	
}