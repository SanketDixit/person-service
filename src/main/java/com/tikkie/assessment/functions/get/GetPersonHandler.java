package com.tikkie.assessment.functions.get;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.tikkie.assessment.functions.BaseHandler;
import com.tikkie.assessment.model.bean.Person;
import com.tikkie.assessment.services.DynamoDBService;
import com.tikkie.assessment.services.impl.DynamoDBServiceImpl;
import software.amazon.awssdk.utils.StringUtils;

/**
 * @author sanket.dixit
 * <p>
 * The GetPersonHandler class is the handler class for the GET /person/{personId} API
 * It is used to get the person by personId
 */
public class GetPersonHandler extends BaseHandler {

    /**
     * The DynamoDBService instance
     */
    private final DynamoDBService dynamoDBService = new DynamoDBServiceImpl();

    /**
     * The handleRequest method is the entry point for the Lambda function
     * It gets the person by personId
     *
     * @param requestEvent The APIGatewayProxyRequestEvent object
     * @param context The Context object
     * @return The APIGatewayProxyResponseEvent object
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        LambdaLogger logger = context.getLogger();

        Person person;

        try {

            if (requestEvent.getPathParameters() == null || requestEvent.getPathParameters().isEmpty()
                    || StringUtils.isBlank(requestEvent.getPathParameters().get("personId"))) {

                logger.log("Person Id is missing in the request!!", LogLevel.ERROR);
                return createErrorResponse("Person Id is missing in the request!!", null, 400);

            }

            person = dynamoDBService
                    .getPersonById(requestEvent.getPathParameters().get("personId"));

            if (person == null) {

                logger.log("Person not found", LogLevel.ERROR);
                return createErrorResponse("Person not found", null, 404);

            }

        } catch (Exception e) {

            logger.log("Error while getting person: " + e.getMessage(), LogLevel.ERROR);
            return createErrorResponse("Error while getting person", e.getMessage(), 500);

        }

        return createResponse(person, 200);
    }

}
