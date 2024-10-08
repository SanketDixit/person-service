package com.tikkie.assessment.functions.add;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.tikkie.assessment.functions.BaseHandler;
import com.tikkie.assessment.model.event.PersonEvent;
import com.tikkie.assessment.model.request.PersonBody;
import com.tikkie.assessment.services.DynamoDBService;
import com.tikkie.assessment.services.SQSService;
import com.tikkie.assessment.services.impl.DynamoDBServiceImpl;
import com.tikkie.assessment.services.impl.SQSServiceImpl;
import com.tikkie.assessment.util.ConversionUtils;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.utils.StringUtils;

import static com.tikkie.assessment.util.Constants.PERSON_CREATED_EVENT_QUEUE;

/**
 * @author sanket.dixit
 * <p>
 * The CreatePersonHandler class is the handler class for the POST /person API
 * It is used to create a new person in the system
 * It also sends the Person Created event to the SQS
 * It also handles the rollback of the transaction in case of any error
 */
public class CreatePersonHandler extends BaseHandler {

    /**
     * The DynamoDBService instance
     */
    private final DynamoDBService dynamoDBService = new DynamoDBServiceImpl();
    /**
     * The SQSService instance
     */
    private final SQSService sqsService = new SQSServiceImpl();

    /**
     * The handleRequest method is the entry point for the Lambda function
     * It creates a new person in the system
     * It sends the Person Created event to the SQS
     * It also handles the rollback of the transaction in case of any error
     *
     * @param requestEvent The APIGatewayProxyRequestEvent object
     * @param context      The Context object
     * @return The APIGatewayProxyResponseEvent object
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        LambdaLogger logger = context.getLogger();

        String personId = null;

        try {

            if (StringUtils.isBlank(requestEvent.getBody())) {
                return createErrorResponse("Request body is empty!!", null, 400);
            }

            PersonBody personBody = ConversionUtils.jsonToObject(requestEvent.getBody(), PersonBody.class);

            if (personBody == null) {
                return createErrorResponse("Person object is empty!!", null, 400);
            }

            if (StringUtils.isBlank(personBody.getFirstName())
                    || StringUtils.isBlank(personBody.getEmail())) {
                return createErrorResponse("First Name and Email are mandatory fields", null, 400);
            }

            personId = dynamoDBService.addPerson(personBody);

            PersonEvent personEvent = PersonEvent.builder()
                    .personId(personId)
                    .firstName(personBody.getFirstName())
                    .lastName(personBody.getLastName())
                    .email(personBody.getEmail())
                    .build();

            SendMessageResponse sendMessageResponse = sqsService.sendMessage(PERSON_CREATED_EVENT_QUEUE, ConversionUtils.objectToJson(personEvent));
            logger.log(String.format("Person created event sent to queue-messageId: %s", sendMessageResponse.messageId()));
            logger.log(String.format("Person created event sent to queue-md5OfMessageBody: %s", sendMessageResponse.md5OfMessageBody()));

            return createResponse("Person created successfully !!!", 201);

        } catch (Exception e) {

            logger.log(String.format("Error while creating person event: %s", e.getLocalizedMessage()),
                    LogLevel.ERROR);

            try {
                if (personId != null) {
                    dynamoDBService.rollbackPerson(personId);
                    logger.log("Stored person data rolled back", LogLevel.INFO);
                }

            } catch (Exception ex) {

                logger.log(ex.getMessage(), LogLevel.ERROR);
                return createErrorResponse("Operation Failed", ex.getMessage(), 500);

            }

            return createErrorResponse("Transaction rolled back", e.getMessage(), 400);

        }

    }

}
