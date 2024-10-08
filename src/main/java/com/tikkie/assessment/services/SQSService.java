package com.tikkie.assessment.services;

import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

/**
 * @author sanket.dixit
 *
 * Service to interact with the SQS
 */
public interface SQSService {

    /**
     * Send a message to the SQS
     *
     * @param queue queue
     * @param message message
     * @return SendMessageResponse
     */
    SendMessageResponse sendMessage(String queue, String message);
}
