package com.tikkie.assessment.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sanket.dixit
 *
 * Person model class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonBody {

    /**
     * First name of the person
     */
    private String firstName;
    /**
     * Last name of the person
     */
    private String lastName;
    /**
     * Email of the person
     */
    private String email;
    /**
     * Phone number of the person
     */
    private String phoneNumber;
    /**
     * Address of the person
     */
    @JsonProperty("address")
    private AddressBody addressBody;

}
