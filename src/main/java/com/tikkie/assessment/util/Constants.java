package com.tikkie.assessment.util;

import lombok.experimental.UtilityClass;

/**
 * @author sanket.dixit
 *
 * Constants class is used to store the constants used in the application.
 */
@UtilityClass
public class Constants {

    public static final String TARGET_PACKAGE_NAME = "target/person-service-1.0.0.jar";

    public static final String PERSON_DYNAMO_DB_TABLE_NAME = "Person";

    public static final String TABLE_NAME = "TABLE_NAME";

    public static final String JAVA_TOOL_OPTIONS = "JAVA_TOOL_OPTIONS";

    public static final String PERSON_CREATED_EVENT_QUEUE = "PersonCreatedEventQueue";

    public static final String PERSON_CREATED_EVENT_DLQ = "PersonCreatedEventDLQ";

    public static final String DATA_INTEGRITY_ALGORITHM = "AES";
}
