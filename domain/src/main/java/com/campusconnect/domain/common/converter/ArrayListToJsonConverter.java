package com.campusconnect.domain.common.converter;

import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import com.campusconnect.domain.user.pojo.BilkenteerPhoneNumbers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.List;

public class ArrayListToJsonConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            if (attribute == null) attribute = new ArrayList<>();
            return new ObjectMapper()
                    .writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return new ArrayList<>();
            }
            return new ObjectMapper().readValue(dbData, ArrayList.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}