package com.tikkie.assessment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tikkie.assessment.exceptions.JsonDeserializationException;
import com.tikkie.assessment.exceptions.JsonSerializationException;
import lombok.experimental.UtilityClass;

import java.io.IOException;

/**
 * @author sanket.dixit
 *
 * Utility class to convert objects to JSON and vice versa.
 */
@UtilityClass
public class ConversionUtils {

    private static final ObjectMapper DEFAULT_MAPPER = createDefaultObjectMapper();

    public static <T> String objectToJson(final T object) {
        try {
            return DEFAULT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static <T> T jsonToObject(final String json, final Class<T> clazz) {
        try {
            return String.class.isAssignableFrom(clazz) ? (T) json : DEFAULT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonDeserializationException(e.getMessage());
        }
    }

    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
