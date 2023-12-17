package com.campusconnect.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

/**
 * Service class responsible for sending emails using JavaMailSender.
 */
@Component
@Slf4j
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a One-Time Password (OTP) email to the specified recipient.
     *
     * @param recipientEmail The email address of the recipient.
     * @param otp             The One-Time Password to include in the email.
     */
    public void sendOTPEmail(String recipientEmail, String otp)  {

        try{

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

        } catch (MessagingException e){

            log.warn("OTP Email to : " + recipientEmail + " Failed! Due to " + e.toString());
        }

    }

    /**
     * Sends a notification email to the specified recipient.
     *
     * @param userName        The name of the user receiving the notification.
     * @param recipientEmail  The email address of the recipient.
     * @param subject         The subject of the notification email.
     * @param content         The content/body of the notification email.
     */
    public void sendNotificationEmail(String userName, String recipientEmail, String subject, String content) {

        try{

            MimeMessage message = mailSender.createMimeMessage();

            message.setFrom("CampusConnect");
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);

            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Notification</title>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; color: #333; }" +
                    ".container { background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    "h1 { color: #1a82e2; font-weight: normal; font-size: 24px; }" +
                    "p { font-size: 16px; line-height: 1.6; }" +
                    ".content { background-color: #eef2f7; border-left: 4px solid #1a82e2; padding: 15px; margin-top: 20px; }" +
                    ".footer { font-size: 12px; color: #777; text-align: center; margin-top: 30px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"container\">" +
                    "<h1>Hi " + userName + "!</h1>" +
                    "<p>You have a new notification!</p>" +
                    "<div class=\"content\">" + content + "</div>" +
                    "<div class=\"footer\">© 2023 Campus Connect. All rights reserved.</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);

            log.info("Notification Email sent Successfully to : " + recipientEmail);

        } catch (MessagingException e){

            log.warn("Notification Email to : " + recipientEmail + " Failed! Due to " + e.toString());
        }

    }
}