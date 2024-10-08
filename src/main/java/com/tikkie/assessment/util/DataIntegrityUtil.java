package com.tikkie.assessment.util;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

import static com.tikkie.assessment.util.Constants.DATA_INTEGRITY_ALGORITHM;

/**
 * @author sanket.dixit
 *
 * DataIntegrityUtil is the utility class to generate the AES key for data integrity.
 */
@UtilityClass
public class DataIntegrityUtil {

    /**
     * The secret key used for encryption
     */
    private static final String SECRET = "dummy-secret-for-encryption";
    /**
     * The salt used for encryption
     */
    private static final String SALT = "tasmanianDevil";
    /**
     * The iteration count used for encryption
     */
    private static final int ITERATION_COUNT = 65536;
    /**
     * The key length used for encryption
     */
    private static final int KEY_LENGTH = 256;

    /**
     * Generate the key
     *
     * @return the secret key
     * @throws Exception if the key generation fails
     */
    public static SecretKey generateKey() throws Exception {

        byte[] saltBytes = SALT.getBytes();

        KeySpec spec = new PBEKeySpec(SECRET.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, DATA_INTEGRITY_ALGORITHM);

    }
}
