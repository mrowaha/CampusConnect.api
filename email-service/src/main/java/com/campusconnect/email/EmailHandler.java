package com.campusconnect.email;
import java.util.logging.Logger;

public class EmailHandler {
    private static final Logger log = Logger.getLogger(EmailHandler.class.getName());
    public static void sendEmail(String content) {
        log.info("Email Sent with content: " + content);
//        System.out.println("Email Sent with content: " + content);
    }
}