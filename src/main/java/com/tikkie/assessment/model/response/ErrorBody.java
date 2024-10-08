package com.tikkie.assessment.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sanket.dixit
 * <p>
 * ErrorBody is the POJO class for the error response
 */
@Builder
@Setter
@Getter
public class ErrorBody {

    /**
     * statusCode is the HTTP status code
     */
    private Integer statusCode;
    /**
     * errorMessage is the error message
     */
    private String errorMessage;
    /**
     * errorKey is the error key
     */
    private String errorKey;
}
