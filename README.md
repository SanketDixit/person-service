# Welcome to CDK Java project!

This project is a simple microservice designed to manage person data using AWS Lambda, DynamoDB, and SQS. 
It features three Lambda functions: 
* **CreatePersonHandler**, which adds new person records to a DynamoDB table and 
publishes a "Person Created" event to an SQS queue; 
* **ListPersonHandler**, which retrieves a list of all persons from the table; 
* and **GetPersonHandler**, which fetches details of a specific person by their personId. 

This setup enables seamless integration with other services that subscribe to the SQS queue, making it ideal 
for scenarios like triggering automated actions upon new record creation.

## Useful commands to execute the project 

* `mvn package`     compile and run tests
* `cdk bootstrap`   required to run only if you are deploying the stack for the first time
* `cdk deploy`      deploy this stack to your default AWS account/region

## Solution Approach & Future Enhancements

### Solution Approach

This project is a simple microservice designed to manage person data using AWS Lambda and DynamoDB, 
with integrated event publishing to an SQS queue. It includes three Lambda functions:

* **CreatePersonLambda**: Handles `POST /persons` API calls, creating new records in DynamoDB and then publishing an 
event to an SQS queue. If an error occurs during the event publishing, but the record is already created in DynamoDB, 
the function rolls back the record creation, ensuring data consistency between the database and the event system. 
This mechanism helps prevent false*positive scenarios where data could be stored without the corresponding event 
being published.

* **ListPersonsLambda**: Handles `GET /persons` API calls to retrieve a list of all stored persons from 
the DynamoDB table. It ensures that only the `personId` and `email` are returned for each person, safeguarding any 
sensitive information from being exposed.

* **GetPersonLambda**: Handles `GET /persons/{personId}` API calls to retrieve details of a specific person from the 
DynamoDB table using their personId.

This design facilitates seamless integration with other microservices that listen for events published on the SQS queue, 
making it ideal for scenarios such as sending automated email notifications or triggering further processing whenever a 
new person record is added. The use of AWS CDK ensures that the service is easily deployable, scalable, and maintainable.

To manage the data integrity of information stored in the DynamoDB table, we implement encryption, as we are handling 
Personally Identifiable Information (PII). For this purpose, we utilize Java Crypto to encrypt and decrypt the data. 
Specifically, we encrypt the data before storing it in the DynamoDB table and decrypt it upon retrieval. 
However, when publishing events to the SQS queue, the data is sent in its unencrypted form.

### Future Enhancements

This project/service can be enhanced in several ways to improve its functionality and security:

* **Data Validation**: Implement data validation checks to ensure that only valid data is stored in the DynamoDB table.
* **Data Integrity**: Enhance data integrity by implementing AWS KMS for encryption and decryption of data stored in 
DynamoDB, ensuring that data is secure both at rest and in transit, as well as during event publishing to the SQS queue. 
This would allow multiple services to access the same encryption and decryption logic.
* **Publishing Events**: Implement a retry mechanism for publishing events to the SQS queue to address scenarios where 
event publishing fails due to network issues or other transient errors.
* **Data Indexing**: Implement indexing on the DynamoDB table to improve query performance when fetching data 
by personId
