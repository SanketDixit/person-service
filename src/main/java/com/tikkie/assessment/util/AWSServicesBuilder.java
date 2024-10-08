package com.tikkie.assessment.util;

import lombok.Builder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * @author sanket.dixit
 *
 * This class is used to build AWS services like DynamoDbClient, DynamoDbEnhancedClient etc.
 */
@Builder
public class AWSServicesBuilder {

    /**
     * DynamoDbClient
     */
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    /**
     * This method is used to build DynamoDbAsyncClient
     * @return DynamoDbAsyncClient
     */
    public static DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

}
