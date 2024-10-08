package com.tikkie.assessment;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awssdk.regions.Region;

/**
 * @author sanket.dixit
 *
 * The APP Initiator of our project
 */
public class PersonServiceApp {
    public static void main(final String[] args) {
        App app = new App();

        new PersonServiceStack(app, "PersonServiceStack", StackProps.builder()
                .env(Environment.builder()
                        .account("Please enter a valid AWS account")
                        .region(Region.EU_CENTRAL_1.id())
                        .build())
                .build());

        app.synth();
    }
}

