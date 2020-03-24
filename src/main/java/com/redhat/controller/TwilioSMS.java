package com.redhat.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMS {
	
	 public static final String ACCOUNT_SID = "AC29abf840738a523026ba84d0ae8f0a70";
	 public static final String AUTH_TOKEN = "67130b52245b29ec7fd0ac3be61d8e91";

	 public static void main(String[]args) {
	  Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	  
	   Message message = Message.creator(
			    new PhoneNumber("+16083584153"),
			    new PhoneNumber("+16088074127"),
			    "Hello world!"
			).create();

			System.out.println("message body: " + message.getBody());
	   System.out.println(message.getSid());
	 }

	 public String  sendSMS(String to, String message){
		 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		 Message msg = Message.creator(
				    new PhoneNumber(to),
				    new PhoneNumber("+16088074127"),
				    message
				).create();
		 	
				System.out.println("message body: " + msg.getBody());
				return "success";
	 }
	 
	 public Message  sendSMSMessage(String to, String message){
		 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		 Message msg = Message.creator(
				    new PhoneNumber("+1"+to),
				    new PhoneNumber("+16088074127"),
				    message
				).create();
		 	
				System.out.println("message body: " + msg.getBody());
				return msg;
	 }

	/* public void sendRestMessage(){
		 
		 HttpPost httpPost = new HttpPost(
	                "https://api.twilio.com/2010-04-01/Accounts/AC29abf840738a523026ba84d0ae8f0a70/Messages");
			 String credentials  = ACCOUNT_SID + ":" + AUTH_TOKEN;
			 String encoded=null;
			try {
				encoded = new org.apache.commons.codec.binary.Base64().encodeAsString(credentials.getBytes("ascii"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 String base64EncodedCredentials =  "Basic " + encoded; // String base64EncodedCredentials = "Basic" + authStringEnc;
			
			 httpPost.setHeader("Authorization",base64EncodedCredentials);

			 CookieStore cookieStore = new BasicCookieStore();
			 HttpContext httpContext = new BasicHttpContext();
			 Cookie cookie = new BasicClientCookie("account", "1001");
			 cookieStore.addCookie(cookie);
			 
			    
			 //cookieStore.addCookie(arg0);
			 httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			 
		  	HttpClient httpclient = HttpClientBuilder.create().build();
		  //	httpclient.setCookieStore(cookieStore);
	        HttpResponse response = null;
	         try {
	        	 httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
	         	
	         	List<NameValuePair> arguments = new ArrayList<NameValuePair>();
	            arguments.add(new BasicNameValuePair("To", "+16083584153"));
	            arguments.add(new BasicNameValuePair("From", "+16088074127"));
	            arguments.add(new BasicNameValuePair("Body", "test"));
	            httpPost.setEntity(new UrlEncodedFormEntity(arguments));
	             response = httpclient.execute(httpPost);
	         } catch (ClientProtocolException cpException) {
	             cpException.printStackTrace();
	         } catch (IOException ioException) {
	             ioException.printStackTrace();
	         }
	  
	         // verify response is HTTP OK
	         final int statusCode = response.getStatusLine().getStatusCode();
	         if (statusCode != HttpStatus.SC_OK) {
	             System.out.println("Error authenticating to Force.com: "+statusCode);
	             // Error is in EntityUtils.toString(response.getEntity())
	             return;
	         }
	  
	         
	         // release connection
	         //httpPost.releaseConnection();
		 
	 }*/
}
