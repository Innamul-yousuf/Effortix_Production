package com.effortix.backend.EmailsUps;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
    	try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("innamulyousufzath.r@intelizign.com");
        mailSender.send(message);
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }
    
    
    public void readEmails() {
        try {
            // Set mail properties
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");  // IMAP protocol
            properties.put("mail.imap.host", "smtp.office365.com");  // IMAP host
            properties.put("mail.imap.port", "587");  // IMAP port for SSL
            properties.put("mail.imap.starttls.enable", "true");

            // Create a session object with authentication


            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("innamulyousufzath.r@intelizign.com", "Iwilldoit@izn02");
                }
            });

            // Connect to the mail store (inbox)
            Store store = session.getStore("imaps");
            store.connect("outlook.office365.com", "innamulyousufzath.r@intelizign.com", "Iwilldoit@izn02");

            // Open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Fetch unread emails (you can customize the search filter)
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            System.out.println("Number of unread emails: " + messages.length);

            // Loop through messages and print details
            for (Message message : messages) {
                MimeMessage mimeMessage = (MimeMessage) message;
                System.out.println("Subject: " + mimeMessage.getSubject());
                System.out.println("From: " + mimeMessage.getFrom()[0]);
                System.out.println("Sent Date: " + mimeMessage.getSentDate());
                System.out.println("Message: " + mimeMessage.getContent().toString());
                System.out.println("---------------------------------");
            }

            // Close the inbox folder and store
            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}