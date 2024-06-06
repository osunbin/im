package com.bin.im.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String toJson(Object resp) {
        try {
           return objectMapper.writeValueAsString(resp);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
           return objectMapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }










    public static <T> T jsonByteToObject(byte[] json,Class<T> clazz) throws IOException {
        return objectMapper.readValue(json,clazz);
    }

    public static Map<String, Object> jsonByteToMap(byte[] json) throws IOException {

        return objectMapper.readValue(json,
                new TypeReference<Map<String, Object>>() {
                });
    }




}
