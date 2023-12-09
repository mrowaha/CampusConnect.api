package com.campusconnect.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger log = Logger.getLogger(EmailSenderService.class.getName());

    public void sendOTPEmail(String recipientEmail, String otp) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("CampusConnect");
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Campus Connect OTP: One-Time Password");

        String htmlContent = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>CampusConnect - One-Time Password</title>" +
                "<style>" +
                "body { font-family: sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }" +
                "h1, h2, h3 { color: #1b77c9; font-weight: bold; margin-bottom: 15px; }" +
                "p { color: #555555; font-size: 16px; line-height: 1.5em; }" +
                ".otp { font-size: 24px; font-weight: bold; text-align: center; margin: 0; padding: 10px; background-color: #1b77c9; color: white; border-radius: 5px; }" +
                ".footer { font-size: 12px; color: #777777; margin-top: 20px; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1 style=\"color: #1b77c9;\">Campus Connect One-Time Password</h1>" +
                "<h2 style=\"color: #555555;\">Thank you for Signing Up at CampusConnect!</h2>" +
                "<p>Please use the following One-Time Password (OTP) to verify your account and complete your login.</p>" +
                "<div class=\"otp\">" + otp + "</div>" +
                "<p>This OTP is valid for 5 minutes. If you don't use it within this time, please request a new one.</p>" +
                "<div class=\"footer\">© 2023 Campus Connect. All rights reserved.</div>" +
                "</body>" +
                "</html>";

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);

        log.info("OTP Email sent Successfully to : " + recipientEmail);
    }

    private String buildNotificationContent(String notificationType, String content) {
        String htmlContent = "";
        if ("MESSAGE_FROM_USER".equals(notificationType)) {
            // Extract relevant information from the content string
            String senderName = (content);
            String messageContent = (content);

            // Build the HTML content
            htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>CampusConnect: New Message from a User</title>" +
                    "<style>" +
                    "body { font-family: sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<h2>You have a new message from " + senderName + "!</h2>" +
                    "<p>" + messageContent + "</p>" +
                    "<p><a href=\"https://campusconnect.com/messages\">View message</a></p>" +
                    "</body>" +
                    "</html>";
        } else if ("PRODUCT_VIEWED".equals(notificationType)) {
            // Implement logic for PRODUCT_VIEWED notification type
        } else if ("TAGGED_IN_POST".equals(notificationType)) {
            // Implement logic for TAGGED_IN_POST notification type
        }
        return htmlContent;
    }
    public void sendNotificationEmail(String recipientEmail, String notificationType, String content) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("CampusConnect");
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientEmail));

        switch (notificationType) {
            case "MESSAGE_FROM_USER":
                message.setSubject("CampusConnect: New Message from a User");
                break;
            case "PRODUCT_VIEWED":
                message.setSubject("CampusConnect: Product You Viewed");
                break;
            case "TAGGED_IN_POST":
                message.setSubject("CampusConnect: You've Been Tagged in a Post");
                break;
            default:
                message.setSubject("CampusConnect: Notification");
                break;
        }

        String htmlContent = buildNotificationContent(notificationType, content);

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);

        log.info("Notification Email sent Successfully to : " + recipientEmail);
    }
}