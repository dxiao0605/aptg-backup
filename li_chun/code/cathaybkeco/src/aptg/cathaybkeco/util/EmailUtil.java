package aptg.cathaybkeco.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aptg.cathaybkeco.config.SysConfig;


public class EmailUtil {
	private static final Logger logger = LogManager.getFormatterLogger(EmailUtil.class.getName());
//	private static final String CLASS_NAME = EmailUtil.class.getSimpleName();
//	private static final Logger logger = LogManager.getFormatterLogger(CLASS_NAME);

//	private String mode = "";
//	private String appLocation = "";
	private String from = "";
	private String subject = "";
	private String body = "";
	private String host = "";
	private String port = "";
	private String user = "";
	private String pwd = "";
	private String attachment = "";
	private List<String> toList = new ArrayList<String>();
	private List<String> ccList = new ArrayList<String>();
	private List<String> bccList = new ArrayList<String>();

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<String> getToList() {
		return toList;
	}
	public void setToList(List<String> toList) {
		this.toList = toList;
	}
	public List<String> getCcList() {
		return ccList;
	}
	public void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}
	public List<String> getBccList() {
		return bccList;
	}
	public void setBccList(List<String> bccList) {
		this.bccList = bccList;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


	public EmailUtil(){	
		try {
			this.host = SysConfig.getInstance().getMailHost();
			this.port = SysConfig.getInstance().getMailPort();
			this.from = SysConfig.getInstance().getMailSender();
			this.user = SysConfig.getInstance().getMailSender();
			this.pwd = SysConfig.getInstance().getMailPassword();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMail() throws MessagingException {
		// Get system properties
		Properties properties = new Properties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		//User Authentication Part
		
		Session session = null;
		if(StringUtils.isNotBlank(pwd)) {
		    properties.put("mail.smtp.starttls.enable", "true");
		    properties.put("mail.smtp.starttls.required", "true");
		    properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		    properties.put("mail.smtp.port", port);
		    properties.put("mail.smtp.auth", "true");

			// Get the default Session object.
			session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(user, pwd);
	            }
	        });
			
		}else {
			 session = Session.getDefaultInstance(properties);	
		}
		
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			for (String to: this.toList) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));	    
			} 

			for (String cc: this.ccList) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));	    
			} 

			for (String bcc: this.bccList) {
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));	    
			} 

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(body);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if (getAttachment() != null && !getAttachment().equals("")) {
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(this.attachment);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(new File(this.attachment).getName());
				multipart.addBodyPart(messageBodyPart);
			}

			// Send the complete message parts
			message.setContent(multipart);

			/*
	         if (isHtmlContent) {
		         // Send the actual HTML message, as big as you like
		         message.setContent(body, "text/html; charset=utf-8"); 
	         } else {
		         // Now set the actual message
		         message.setText(body);        
	         }
			 */

			// Send message
			Transport.send(message);
			Address[] recipients = message.getAllRecipients();
			for (Address a: recipients) {
				logger.debug("Recipient:" + a.toString());
			}	         
			logger.debug("Subject:" + subject + " Send message success." + "(next hop:" + host + ")");
		}catch (MessagingException mex) {
			throw new MessagingException(mex.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException, MessagingException {
		List<String> receivers = new ArrayList<>();
//		receivers.add("austinchen@aptg.com.tw");
		receivers.add("lichiunhsu@aptg.com.tw");
		
		String subject = "為您重置密碼";
		String body = "嗨 Freestyle-TW \r\n" + 
					  "有收到重置您密碼的申請。 \r\n" + 
					  "您的新密碼為: \r\n" + 
					  "此密碼將在72小時後逾期\r\n" + 
					  "這是自動發送的訊息，請勿回覆。"; 
		
		EmailUtil email = new EmailUtil();
		email.setToList(receivers);	// 設定收件者
		email.setSubject(subject);	// 設定信件主旨
		email.setBody(body);	// 設定信件內容
		email.sendMail();
	}
}
