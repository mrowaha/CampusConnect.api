package com.campusconnect.email.otp;

import java.security.SecureRandom;

public interface OTPStrategy {
    String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom RANDOM = new SecureRandom();

     static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ALPHANUMERIC.length());
            char randomChar = ALPHANUMERIC.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    String setOTPForEmail(String email);

    String getOTPForEmail(String email);
}
