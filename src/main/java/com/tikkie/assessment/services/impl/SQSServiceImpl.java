package com.tikkie.assessment.services.impl;

import com.tikkie.assessment.services.SQSService;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

/**
 * @author sanket.dixit
 *
 * The SQSServiceImpl class is the implementation class for the SQSService
 */
public class SQSServiceImpl implements SQSService {

    private final SqsClient sqsClient = SqsClient.builder().build();

    /**
     * {@inheritDoc}
     */
    @Override
    public SendMessageResponse sendMessage(String queue, String message) {
        try {
        SendMessageRequest sendMessageRequest = SendMessageRequest
                .builder()
                .queueUrl(queue)
                .messageBody(message)
                .build();
        return sqsClient.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
