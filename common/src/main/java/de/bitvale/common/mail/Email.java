package de.bitvale.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class Email {

    private static final Logger log = LoggerFactory.getLogger(Email.class);

    @Resource(name = "java:jboss/mail/Default")
    Session session;

    public void send(String to, String subject, String body) {

        try {
            InternetAddress toAddress = InternetAddress.parse(to)[0];
            InternetAddress fromAddress = InternetAddress.parse("anjunar@gmx.de")[0];

            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setFrom(fromAddress);
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            log.error(e.getLocalizedMessage());
        }

    }

}
