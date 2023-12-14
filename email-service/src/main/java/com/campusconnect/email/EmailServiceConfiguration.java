package com.campusconnect.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailServiceConfiguration {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //Dependency Clash with @autowiring from application.properites. Hence, values hard coded
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("campusconnect.noreply@gmail.com");
        mailSender.setPassword("gfmi ttkn tzjz nchw");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", String.valueOf(true));
        props.put("mail.smtp.starttls.enable", String.valueOf(true));

        return mailSender;
    }
}

