package com.tikkie.assessment.util;

import com.tikkie.assessment.model.event.PersonEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author sanket.dixit
 */
class ConversionUtilsTest {

    @Test
    public void objectToJson() {
        PersonEvent personEvent = PersonEvent.builder()
                .personId("e034dcbe-7405-44d6-8ec5-2418559ae9ac")
                .firstName("IronMan")
                .lastName("Tony")
                .email("tony.stark@yah.com")
                .build();

        String json = ConversionUtils.objectToJson(personEvent);

        assertNotNull(json);
        assertEquals("{\"personId\":\"e034dcbe-7405-44d6-8ec5-2418559ae9ac\"," +
                "\"firstName\":\"IronMan\",\"lastName\":\"Tony\"," +
                "\"email\":\"tony.stark@yah.com\"}", json);
    }

    @Test
    public void jsonToObject() {
        String json = "{\"personId\":\"e034dcbe-7405-44d6-8ec5-2418559ae9ac\"," +
                "\"firstName\":\"IronMan\",\"lastName\":\"Tony\"," +
                "\"email\":\"tony.stark@yah.com\"}";

        PersonEvent personEvent = ConversionUtils.jsonToObject(json, PersonEvent.class);

        assertNotNull(personEvent);
        assertEquals("e034dcbe-7405-44d6-8ec5-2418559ae9ac", personEvent.getPersonId());
        assertEquals("IronMan", personEvent.getFirstName());
        assertEquals("Tony", personEvent.getLastName());
        assertEquals("tony.stark@yah.com", personEvent.getEmail());
    }
}
