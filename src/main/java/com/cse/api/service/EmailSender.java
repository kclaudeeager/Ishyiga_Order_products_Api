package com.cse.api.service;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
	@Autowired
	private JavaMailSender mailSender;
	public void SendSimpleEmail(String toEmail,String body,String subject) {
	SimpleMailMessage message=new SimpleMailMessage();
	message.setTo(toEmail);
	message.setSubject(subject);
	message.setText(body);
	message.setFrom("ishyigakwizera@gmail.com");
	
	mailSender.send(message);
	
	System.out.println("Sending email..");
	
	

}
public void semdEmailWithAttachement(String toEmail,String body,String subject,String attachement) {
	MimeMessage mimemessage=mailSender.createMimeMessage();
	try {
		MimeMessageHelper messageHelper=new MimeMessageHelper(mimemessage,true);
		messageHelper.setFrom("ishyigakwizera@gmail.com");
		messageHelper.setTo(toEmail);
		messageHelper.setText(body);
		messageHelper.setSubject(subject);
		messageHelper.setSentDate(new Date());
		FileSystemResource fileSystem=new FileSystemResource(new File(attachement));
		messageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
		mailSender.send(mimemessage);
		System.out.println("Sending attachement email..");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
