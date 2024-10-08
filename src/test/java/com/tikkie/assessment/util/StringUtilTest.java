package com.tikkie.assessment.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sanket.dixit
 */
class StringUtilTest {

    @Test
    void getValue() {
        assertEquals("", StringUtil.getValue(null));
        assertEquals("test", StringUtil.getValue("test"));
    }

}
