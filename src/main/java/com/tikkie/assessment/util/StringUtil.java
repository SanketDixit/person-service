package com.tikkie.assessment.util;

import lombok.experimental.UtilityClass;

/**
 * @author sanket.dixit
 *
 * StringUtil is the utility class for the String operations
 */
@UtilityClass
public class StringUtil {

    /**
     * Returns the value if not null, else returns empty string
     *
     * @param value input value
     * @return value if not null, else empty string
     */
    public static String getValue(String value) {
        return value == null ? "" : value;
    }
}
