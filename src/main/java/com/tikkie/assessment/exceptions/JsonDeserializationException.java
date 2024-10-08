package com.tikkie.assessment.exceptions;

/**
 * @author sanket.dixit
 * <p>
 * JsonDeserializationException is the exception thrown when there is an issue with deserialization of the JSON.
 */
public class JsonDeserializationException extends RuntimeException {

    /**
     * Constructor
     *
     * @param message    message
     * @param parameters parameters
     */
    public JsonDeserializationException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
