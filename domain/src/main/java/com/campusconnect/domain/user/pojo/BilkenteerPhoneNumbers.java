package com.campusconnect.domain.user.pojo;


import java.util.ArrayList;

public class BilkenteerPhoneNumbers {

    private final ArrayList<String> phoneNumbers;

    public BilkenteerPhoneNumbers() {
        this.phoneNumbers = new ArrayList<>();
    }

    public BilkenteerPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
