package com.tikkie.assessment.services.impl;

import com.tikkie.assessment.model.bean.Address;
import com.tikkie.assessment.model.bean.Person;
import com.tikkie.assessment.model.request.AddressBody;
import com.tikkie.assessment.model.request.PersonBody;
import com.tikkie.assessment.services.DataIntegrityService;
import com.tikkie.assessment.services.DynamoDBService;
import com.tikkie.assessment.util.AWSServicesBuilder;
import com.tikkie.assessment.util.DataIntegrityUtil;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tikkie.assessment.util.StringUtil.getValue;

/**
 * @author sanket.dixit
 * <p>
 * The DynamoDBServiceImpl class implements the DynamoDBService interface and provides the implementation for the
 * CRUD operations on the Person DynamoDB Table
 */
@Slf4j
public class DynamoDBServiceImpl implements DynamoDBService {

    /**
     * DynamoDB Table for Person
     */
    private final DynamoDbTable<Person> personTable;
    /**
     * Data Integrity Service
     */
    private final DataIntegrityService dataIntegrityService = new DataIntegrityServiceImpl();

    /**
     * Constructor
     */
    public DynamoDBServiceImpl() {
        this.personTable = AWSServicesBuilder
                .dynamoDbEnhancedClient()
                .table("Person", TableSchema.fromBean(Person.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> getAllPersons() {

        List<Person> persons = new ArrayList<>();

        personTable.scan().items().forEach(person -> {

            try {

                SecretKey secretKey = DataIntegrityUtil.generateKey();
                Person decryptedPerson = new Person();
                decryptedPerson.setPersonId(person.getPersonId());
                decryptedPerson.setEmail(dataIntegrityService.decryptData(person.getEmail(), secretKey));

                persons.add(decryptedPerson);

            } catch (Exception e) {

                log.error("Error while decrypting person data: {}", e.getMessage());
                throw new RuntimeException(String.format("Error while decrypting person data: %s", e.getMessage()));

            }

        });

        return persons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPersonById(String personId) {

        Person person = null;

        Person decryptedPerson = personTable.getItem(generatePersonKey(personId));

        if (decryptedPerson != null) {

            try {

                person = new Person();
                SecretKey secretKey = DataIntegrityUtil.generateKey();
                person.setPersonId(decryptedPerson.getPersonId());
                person.setFirstName(dataIntegrityService.decryptData(decryptedPerson.getFirstName(), secretKey));
                person.setLastName(dataIntegrityService.decryptData(decryptedPerson.getLastName(), secretKey));
                person.setEmail(dataIntegrityService.decryptData(decryptedPerson.getEmail(), secretKey));
                person.setPhoneNumber(dataIntegrityService.decryptData(decryptedPerson.getPhoneNumber(), secretKey));

                Address address = decryptedPerson.getAddress();
                if (address != null) {
                    Address decryptedAddress = new Address();
                    decryptedAddress.setStreet(dataIntegrityService.decryptData(address.getStreet(), secretKey));
                    decryptedAddress.setHouseNumber(dataIntegrityService.decryptData(address.getHouseNumber(), secretKey));
                    decryptedAddress.setCity(dataIntegrityService.decryptData(address.getCity(), secretKey));
                    decryptedAddress.setPostalCode(dataIntegrityService.decryptData(address.getPostalCode(), secretKey));
                    decryptedAddress.setCountry(dataIntegrityService.decryptData(address.getCountry(), secretKey));

                    person.setAddress(decryptedAddress);
                }

            } catch (Exception e) {

                log.error("Error while decrypting person data: {}", e.getMessage());
                throw new RuntimeException(String.format("Error while decrypting person data: %s", e.getMessage()));

            }

        }

        return person;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addPerson(PersonBody personBody) {

        Person person = new Person();
        String personId = UUID.randomUUID().toString();
        person.setPersonId(personId);

        try {

            SecretKey secretKey = DataIntegrityUtil.generateKey();
            person.setFirstName(dataIntegrityService.encryptData(getValue(personBody.getFirstName()), secretKey));
            person.setLastName(dataIntegrityService.encryptData(getValue(personBody.getLastName()), secretKey));
            person.setEmail(dataIntegrityService.encryptData(getValue(personBody.getEmail()), secretKey));
            person.setPhoneNumber(dataIntegrityService.encryptData(getValue(personBody.getPhoneNumber()), secretKey));

            if (personBody.getAddressBody() != null) {

                AddressBody addressBody = personBody.getAddressBody();

                Address address = new Address();
                address.setStreet(dataIntegrityService.encryptData(getValue(addressBody.getStreet()), secretKey));
                address.setHouseNumber(dataIntegrityService.encryptData(getValue(addressBody.getHouseNumber()), secretKey));
                address.setCity(dataIntegrityService.encryptData(getValue(addressBody.getCity()), secretKey));
                address.setPostalCode(dataIntegrityService.encryptData(getValue(addressBody.getPostalCode()), secretKey));
                address.setCountry(dataIntegrityService.encryptData(getValue(addressBody.getCountry()), secretKey));

                person.setAddress(address);

            }

            personTable.putItem(person);

        } catch (Exception e) {

            log.error("Error while adding person data: {}", e.getMessage());
            throw new RuntimeException(String.format("Error while adding person data: %s", e.getMessage()));

        }

        return personId;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollbackPerson(String personId) {

        try {

            personTable.deleteItem(generatePersonKey(personId));

        } catch (Exception e) {

            log.error("Error while rolling back person data: {}", e.getMessage());
            throw new RuntimeException(String.format("Error while rolling back person data: %s", e.getMessage()));

        }

    }

    /**
     * Generate the key for the person
     *
     * @param personId personId
     * @return Key
     */
    private Key generatePersonKey(String personId) {
        return Key.builder().partitionValue(personId).build();
    }

}
