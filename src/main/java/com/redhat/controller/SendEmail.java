package com.redhat.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendEmail {

	public static void main(String[] args) {
        SendEmail demo = new SendEmail();
	    String to = "jpaulraj@redhat.com";
	    String subject = "Important Message";
	    String body = "This is a important message with attachment.";
/*	    EmailNotification notification = new EmailNotification ();
	    notification.setBody(body);
	    notification.setEmailId(to);*/
	    
	   // demo.sendEmail(notification);
        //demo.sendEmail(to ,  subject,  body);
    }
	
	 public void sendEmail(List messages) {
	        // Defines the E-Mail information.
	        String from = "jeyredhat2018@gmail.com";
	        String to =  (String)messages.get(0);
	        
	        String bodyText =  (String)messages.get(1);
	        String subject = (String)messages.get(2);

	        // The attachment file name.
	        //String attachmentName = "/Users/jpaulraj/Document/CustomerSuccessTouchpointAgendaDate-Template.docx";

	        // Creates a Session with the following properties.
	        /*Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.port", "587");*/
	        
	        Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
	       // Session session = Session.getDefaultInstance(props);
	        Session session = Session.getInstance(props,
	                new javax.mail.Authenticator() {
	                   protected PasswordAuthentication getPasswordAuthentication() {
	                      return new PasswordAuthentication("x@gmail.com", "redhat2018");
	       	   }
	                });

	        try {
	            InternetAddress fromAddress = new InternetAddress(from,subject);
	            InternetAddress toAddress = new InternetAddress(to);

	            // Create an Internet mail msg.
	            MimeMessage msg = new MimeMessage(session);
	            msg.setFrom(fromAddress);
	            msg.setRecipient(Message.RecipientType.TO, toAddress);
	            msg.setSubject(subject);
	            msg.setSentDate(new Date());

	            // Set the email msg text.
	            MimeBodyPart messagePart = new MimeBodyPart();
	            messagePart.setText(bodyText);

	            
	            // Create Multipart E-Mail.
	            Multipart multipart = new MimeMultipart();
	            multipart.addBodyPart(messagePart);
	            //multipart.addBodyPart(attachmentPart);

	            msg.setContent(multipart);

	            // Send the msg. Don't forget to set the username and password
	            // to authenticate to the mail server.
	           // Transport.send(msg, "jeyredhat2018", "redhat2018");
	            Transport.send(msg);
	            
	            
	            
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception  ex) {
				
				System.out.println("unable to send email due mail server or internet issue");
			}
	    }

    public void sendEmailwithAttachment() {
        // Defines the E-Mail information.
        String from = "jeyredhat2018@gmail.com";
        String to = "jpaulraj@redhat.com";
        String subject = "Important Message";
        String bodyText = "This is a important message with attachment.";

        // The attachment file name.
        String attachmentName = "/Users/jpaulraj/Document/CustomerSuccessTouchpointAgendaDate-Template.docx";

        // Creates a Session with the following properties.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(props);

        try {
            InternetAddress fromAddress = new InternetAddress(from);
            InternetAddress toAddress = new InternetAddress(to);

            // Create an Internet mail msg.
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(fromAddress);
            msg.setRecipient(Message.RecipientType.TO, toAddress);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Set the email msg text.
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(bodyText);

            // Set the email attachment file
            FileDataSource fileDataSource = new FileDataSource(attachmentName);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(fileDataSource.getName());

            // Create Multipart E-Mail.
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);

            msg.setContent(multipart);

            // Send the msg. Don't forget to set the username and password
            // to authenticate to the mail server.
           // Transport.send(msg, "jeyredhat2018", "");
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
