package com.tikkie.assessment.services;

import com.tikkie.assessment.model.bean.Person;
import com.tikkie.assessment.model.request.PersonBody;

import java.util.List;

/**
 * @author sanket.dixit
 *
 * Service to interact with the DynamoDB
 */
public interface DynamoDBService {

    /**
     * This method will return all the persons stored in the Person DynamoDB Table
     *
     * @return List of persons
     */
    List<Person> getAllPersons();

    /**
     * Get a person by personId
     *
     * @param personId personId
     * @return Person
     */
    Person getPersonById(String personId);

    /**
     * Add a person to the DynamoDB
     *
     * @param person PersonBody
     * @return personId
     */
    String addPerson(PersonBody person);

    /**
     * Rollback the person in case of failed Event creation
     *
     * @param personId personId
     */
    void rollbackPerson(String personId);
}
