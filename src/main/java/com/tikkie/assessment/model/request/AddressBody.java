package com.tikkie.assessment.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sanket.dixit
 *
 * Address model class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBody {

    /**
     * Street name
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
