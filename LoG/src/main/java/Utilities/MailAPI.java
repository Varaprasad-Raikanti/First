package Utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.testng.Assert;


public class MailAPI {
	public static String gmailAPI(String emailID,String password,String fromAddress, String toAddress,String subject,String content) throws Exception
	{
		/*String emailID="mahendra.lotuswave@gmail.com";
		String password="Mahendra@123";
		String fromAddress="Akhil Meesarakonda <meesarakonda.akhil9@gmail.com>";
		String toAddress="Mahendra.lotuswave@gmail.com";
		String subject="Fwd: Reset your OXO password";
		String content="Set a New Password";*/
		SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		boolean toFlag = true, fromFlag = true, subFlag = true;
		Multipart mp = null;
		MimeBodyPart part =new MimeBodyPart();
		Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.gmail.com");
		properties.put("mail.imap.port", "993");
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port",
				String.valueOf("993"));
		try {
			Session session = Session.getInstance(properties);
			Store store = session.getStore("imap");
			store.connect("imap.gmail.com", emailID,password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			Message[] msgs = inbox.getMessages();
			int i = msgs.length;
			System.out.println("Total Mails in Inbox: "+i);
			Message actualMail = null;
			for(i=msgs.length-1;i!=0;i--){
//			for (Message msg : msgs) {
				//System.out.println("Checking mail: "+i--);
				Address[] to = msgs[i].getAllRecipients();
				Address[] in = msgs[i].getFrom();
				Date date = parser.parse(msgs[i].getReceivedDate().toString());
				if(formatter.format(date).equals(formatter.format(today.getTime())))
				{
					if(toAddress!=null)
					{	
						toFlag = false;
						for (Address address : to) {
							System.out.println("To: " + address.toString());
							if(address.toString().equals(toAddress))
								toFlag = true;
						}
					}
					if(fromAddress!=null)
					{
						fromFlag = false;
						for (Address address : in) {
							System.out.println("FROM: " + address.toString());
							if(address.toString().equals(fromAddress))
								fromFlag = true;
						}
					}
					if(fromFlag&&subject!=null&&msgs[i].getSubject()!=null)
					{
						subFlag = false;
						System.out.println("Subject: "+msgs[i].getSubject());
						if(msgs[i].getSubject().equals(subject))
							subFlag = true;
					}
					if(toFlag&&fromFlag&&subFlag)
					{
						actualMail = msgs[i];
						System.out.println("Found expected mail");
						System.out.println(msgs[i].getReceivedDate());
										break;
					}
				}
			}
			if ( actualMail.getContent() instanceof String)  
			{  
				String body = (String)content; 
				System.out.println("Mail Content :  "+body); 
				Assert.assertTrue(body.contains(content));
				part = null;
			} 
			else 
			{
				
				mp = (Multipart) actualMail.getContent();
				part = getBodyPart((MimeBodyPart) mp.getBodyPart(0));
				
			}
			//Delete Mail
//			Folder trash = store.getFolder("[Gmail]/Trash");
//			inbox.copyMessages(new Message[] { actualMail }, trash);
		}catch (Exception mex) {
			mex.printStackTrace();
		}
		return part.getContent().toString();
	}
	
	public static MimeBodyPart gmailAPI(String datafile,String dataset,String toAddress) throws Exception
	{
	
		String content="";
		String emailID="mahendra.lotuswave@gmail.com";
		String password="Mahendra@123";
		String fromAddress="no-reply@accounts.google.com";
		String subject="Security alert";
		SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		boolean toFlag = true, fromFlag = true, subFlag = true;
		Multipart mp = null;
		MimeBodyPart part =new MimeBodyPart();
		Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.gmail.com");
		properties.put("mail.imap.port", "993");
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port",
				String.valueOf("993"));
		try {
			Session session = Session.getInstance(properties);
			Store store = session.getStore("imap");
			store.connect("imap.gmail.com", emailID,password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			Message[] msgs = inbox.getMessages();
			int i = msgs.length;
			System.out.println("Total Mails in Inbox: "+i);
			Message actualMail = null;
			for(i=msgs.length-1;i!=0;i--){
//			for (Message msg : msgs) {
				//System.out.println("Checking mail: "+i--);
				Address[] to = msgs[i].getAllRecipients();
				Address[] in = msgs[i].getFrom();
				Date date = parser.parse(msgs[i].getReceivedDate().toString());
				if(formatter.format(date).equals(formatter.format(today.getTime())))
				{
					if(toAddress!=null)
					{	
						toFlag = false;
						for (Address address : to) {
							System.out.println("To: " + address.toString());
							if(address.toString().equals(toAddress))
								toFlag = true;
						}
					}
					if(fromAddress!=null)
					{
						fromFlag = false;
						for (Address address : in) {
							System.out.println("FROM: " + address.toString());
							if(address.toString().equals(fromAddress))
								fromFlag = true;
						}
					}
					if(fromFlag&&subject!=null&&msgs[i].getSubject()!=null)
					{
						subFlag = false;
						System.out.println("Subject: "+msgs[i].getSubject());
						if(msgs[i].getSubject().equals(subject))
							subFlag = true;
					}
					if(toFlag&&fromFlag&&subFlag)
					{
						actualMail = msgs[i];
						System.out.println("Found expected mail");
						System.out.println(msgs[i].getReceivedDate());
										break;
					}
				}
			}
			if ( actualMail.getContent() instanceof String)  
			{  
				String body = (String)content; 
				System.out.println("Mail Content :  "+body); 
				Assert.assertTrue(body.contains(content));
				part = null;
			} 
			else 
			{
				
				mp = (Multipart) actualMail.getContent();
				part = getBodyPart((MimeBodyPart) mp.getBodyPart(0));
				
			}
			//Delete Mail
//			Folder trash = store.getFolder("[Gmail]/Trash");
//			inbox.copyMessages(new Message[] { actualMail }, trash);
		}catch (Exception mex) {
			mex.printStackTrace();
		}
		return part;
	}
	
	
	public static MimeBodyPart getBodyPart(MimeBodyPart part) throws MessagingException, IOException{
		if (part.getContent() instanceof MimeMultipart){
			return  getBodyPart((MimeBodyPart) ((MimeMultipart) part.getContent()).getBodyPart(0));
        }
		else
			return part;
		
	}
	
	public static void main(String[] args) throws Exception {
		//System.out.println(MailAPI.gmailAPI().split("Set a New Password")[1].split("<")[1].split(">")[0].trim());
	}


}
