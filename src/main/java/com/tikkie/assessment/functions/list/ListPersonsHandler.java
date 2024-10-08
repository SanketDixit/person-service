package com.tikkie.assessment.functions.list;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.tikkie.assessment.functions.BaseHandler;
import com.tikkie.assessment.model.bean.Person;
import com.tikkie.assessment.services.DynamoDBService;
import com.tikkie.assessment.services.impl.DynamoDBServiceImpl;

import java.util.List;

/**
 * @author sanket.dixit
 * <p>
 * The ListPersonsHandler class is the handler class for the GET /person API
 * to list all the persons.
 */
public class ListPersonsHandler extends BaseHandler {

    /**
     * The DynamoDBService instance
     */
    private final DynamoDBService dynamoDBService = new DynamoDBServiceImpl();

    /**
     * The handleRequest method is the entry point for the Lambda function
     * It lists all the persons
     *
     * @param apiGatewayProxyRequestEvent The APIGatewayProxyRequestEvent object
     * @param context                     The Context object
     * @return The APIGatewayProxyResponseEvent object
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        LambdaLogger logger = context.getLogger();

        List<Person> persons;

        try {

            persons = dynamoDBService.getAllPersons();

            if (persons.isEmpty()) {

                logger.log("No persons found", LogLevel.WARN);
                return createErrorResponse("No persons found", null, 404);

            }
        } catch (Exception e) {

            logger.log("Error occurred while fetching persons", LogLevel.ERROR);
            return createErrorResponse("Error occurred while fetching persons", e.getMessage(), 500);

        }

        return createResponse(persons, 200);

    }

}
