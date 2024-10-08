package com.tikkie.assessment.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/**
 * @author sanket.dixit
 *
 * Address model class
 */
@Getter
@Setter
@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    /**
     * Street
     */
    private String street;
    /**
     * House number
     */
    private String houseNumber;
    /**
     * City
     */
    private String city;
    /**
     * Postal code
     */
    private String postalCode;
    /**
     * Country
     */
    private String country;

}
