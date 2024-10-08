package com.tikkie.assessment;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.*;

import static com.tikkie.assessment.util.Constants.*;

/**
 * @author sanket.dixit
 *
 * Person Service Stack is used to create the over all stack of this small project.
 * This stack is responsible for creating the DynamoDB table for storing the Person info,
 * API Gateway for exposing the Person related APIs and Lambdas for handling the API requests.
 * This stack also creates the SQS queue for the Person created event.
 */
public class PersonServiceStack extends Stack {

    public PersonServiceStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Defining the DynamoDB table for storing Persons info.
        Table personsTable = Table.Builder.create(this, PERSON_DYNAMO_DB_TABLE_NAME)
                .partitionKey(Attribute.builder()
                        .name("personId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .tableName(PERSON_DYNAMO_DB_TABLE_NAME)
                .build();

        // Define API Gateway
        RestApi personApiGateway = RestApi.Builder.create(this, "PersonServiceApi")
                .restApiName("Person Service API")
                .description("API for managing different person related functions.")
                .build();

        // Defining all the Lambdas
        Map<String, String> environmentVariables = new HashMap<>();
        environmentVariables.put(TABLE_NAME, personsTable.getTableName());
        environmentVariables.put(JAVA_TOOL_OPTIONS, "-XX:+UseSerialGC -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Xmx512m -Xms512m");

        // Creation of Persons Resource for all the interactions
        Resource personsResource = personApiGateway.getRoot().addResource("persons");

        // Lambda for GET /persons (list all persons)
        Function listPersonsFunction = Function.Builder.create(this, "ListPersonsFunction")
                .runtime(Runtime.JAVA_11)
                .handler("com.tikkie.assessment.functions.list.ListPersonsHandler::handleRequest")
                .code(Code.fromAsset(TARGET_PACKAGE_NAME))
                .environment(environmentVariables)
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build();
        LambdaIntegration getPersonsIntegration = LambdaIntegration.Builder.create(listPersonsFunction).build();
        personsResource.addMethod("GET", getPersonsIntegration);
        personsTable.grantReadData(listPersonsFunction);

        // Lambda for GET /persons/{personId}
        Function getPersonFunction = Function.Builder.create(this, "GetPersonFunction")
                .runtime(Runtime.JAVA_11)
                .handler("com.tikkie.assessment.functions.get.GetPersonHandler::handleRequest")
                .code(Code.fromAsset(TARGET_PACKAGE_NAME))
                .environment(environmentVariables)
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build();
        Resource personIdResource = personsResource.addResource("{personId}");
        LambdaIntegration getPersonByIdIntegration = LambdaIntegration.Builder.create(getPersonFunction).build();
        personIdResource.addMethod("GET", getPersonByIdIntegration);
        personsTable.grantReadData(getPersonFunction);

        // Event Queue
        Queue deadLetterQueue = Queue.Builder.create(this, "PersonCreatedEventDLQ")
                .queueName(PERSON_CREATED_EVENT_DLQ) // Optional: Set DLQ name
                .build();

        Queue personCreatedEventQueue = Queue.Builder.create(this, "PersonCreatedEventQueue")
                .queueName(PERSON_CREATED_EVENT_QUEUE)
                .deadLetterQueue(DeadLetterQueue.builder()
                        .queue(deadLetterQueue)
                        .maxReceiveCount(2)
                        .build())
                .visibilityTimeout(Duration.minutes(10))
                .build();

        // Lambda for POST /persons
        Function addPersonFunction = Function.Builder.create(this, "AddPersonFunction")
                .runtime(Runtime.JAVA_11)
                .handler("com.tikkie.assessment.functions.add.CreatePersonHandler::handleRequest")
                .code(Code.fromAsset(TARGET_PACKAGE_NAME))
                .environment(Map.of(
                        TABLE_NAME, personsTable.getTableName(),
                        PERSON_CREATED_EVENT_QUEUE, personCreatedEventQueue.getQueueName()))
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build();
        LambdaIntegration postPersonIntegration = LambdaIntegration.Builder.create(addPersonFunction).build();
        personsResource.addMethod("POST", postPersonIntegration);
        personsTable.grantWriteData(addPersonFunction);
        personCreatedEventQueue.grantSendMessages(addPersonFunction);
        addPersonFunction.addEventSource(new SqsEventSource(personCreatedEventQueue));

    }
}
