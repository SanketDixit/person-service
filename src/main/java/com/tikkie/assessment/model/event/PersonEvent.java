package com.tikkie.assessment.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author sanket.dixit
 *
 * The PersonEvent class is the model class for the Person Event
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonEvent {

    /**
     * personId
     */
    private String personId;
    /**
     * firstName
     */
    private String firstName;
    /**
     * lastName
     */
    private String lastName;
    /**
     * email
     */
    private String email;

}
