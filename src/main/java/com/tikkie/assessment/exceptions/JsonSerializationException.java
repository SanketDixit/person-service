package com.tikkie.assessment.exceptions;

/**
 * @author sanket.dixit
 * <p>
 * JsonSerializationException is the exception thrown when there is an issue with serialization of the JSON.
 */
public class JsonSerializationException extends RuntimeException {

    /**
     * Constructor
     *
     * @param message    message
     * @param parameters parameters
     */
    public JsonSerializationException(String message, Object... parameters) {
        super(String.format(message, parameters));
    }
}
