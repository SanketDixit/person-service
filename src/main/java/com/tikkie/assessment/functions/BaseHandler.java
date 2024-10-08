package com.tikkie.assessment.functions;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.tikkie.assessment.util.ConversionUtils;
import com.tikkie.assessment.model.response.ErrorBody;

/**
 * @author sanket.dixit
 * <p>
 * BaseHandler is the Abstract layer for all the Lambda functions
 */
public abstract class BaseHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    /**
     * Creates a response with content object & status code
     *
     * @param object     any content object
     * @param statusCode http status code
     * @return response
     */
    protected APIGatewayProxyResponseEvent createResponse(final Object object, final int statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(ConversionUtils.objectToJson(object));
    }

    /**
     * Creates a response with error message & status code
     *
     * @param errorMessage error message
     * @param statusCode   http status code
     * @return response
     */
    protected APIGatewayProxyResponseEvent createErrorResponse(final String errorMessage, final String errorKey,
                                                               final int statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(ConversionUtils.objectToJson(ErrorBody.builder()
                        .statusCode(statusCode)
                        .errorKey(errorKey)
                        .errorMessage(errorMessage)
                        .build()));
    }
}
