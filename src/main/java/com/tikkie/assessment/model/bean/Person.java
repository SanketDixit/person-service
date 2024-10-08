package com.tikkie.assessment.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * @author sanket.dixit
 *
 * Person dynamo db bean class
 */
@Getter
@Setter
@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    /**
     * Person id
     */
    private String personId;
    /**
     * First name
     */
    private String firstName;
    /**
     * Last name
     */
    private String lastName;
    /**
     * Email
     */
    private String email;
    /**
     * Phone number
     */
    private String phoneNumber;
    /**
     * Address
     */
    private Address address;

    @DynamoDbPartitionKey
    public String getPersonId() {
        return personId;
    }

}
