package com.tikkie.assessment.services.impl;

import com.tikkie.assessment.services.DataIntegrityService;
import com.tikkie.assessment.util.DataIntegrityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author sanket.dixit
 */
class DataIntegrityServiceImplTest {

    @Mock
    private DataIntegrityService dataIntegrityService;

    @BeforeEach
    void setUp() {
        dataIntegrityService = new DataIntegrityServiceImpl();
    }

    @Test
    void encryptAndDecryptData() throws Exception {
        String data = "Hello World";

        SecretKey key = DataIntegrityUtil.generateKey();

        String encryptedData = dataIntegrityService.encryptData(data, key);

        assertNotNull(encryptedData);

        String decryptedData = dataIntegrityService.decryptData(encryptedData, key);

        assertNotNull(decryptedData);
        assertEquals(data, decryptedData);
    }
}
