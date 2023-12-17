package com.campusconnect.domain.user.converter;

import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class BilkenteerAddressConverter implements AttributeConverter<BilkenteerAddress, String> {


    @Override
    public String convertToDatabaseColumn(BilkenteerAddress attribute) {
        try {
            if (attribute == null) attribute = new BilkenteerAddress();
            return new ObjectMapper()
                    .writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public BilkenteerAddress convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return new BilkenteerAddress();
            }
            return new ObjectMapper().readValue(dbData, BilkenteerAddress.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
