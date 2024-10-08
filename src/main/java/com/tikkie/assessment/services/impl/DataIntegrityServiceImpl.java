package com.tikkie.assessment.services.impl;

import com.tikkie.assessment.services.DataIntegrityService;
import software.amazon.awssdk.utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

import static com.tikkie.assessment.util.Constants.DATA_INTEGRITY_ALGORITHM;

/**
 * @author sanket.dixit
 *
 * DataIntegrityServiceImpl is the implementation class for the DataIntegrityService
 */
public class DataIntegrityServiceImpl implements DataIntegrityService {

    /**
     * {@inheritDoc}
     */
    @Override
    public String encryptData(String data, SecretKey secretKey) throws Exception {

        if (StringUtils.isBlank(data)) {
            return null;
        }

        Cipher cipher = Cipher.getInstance(DATA_INTEGRITY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decryptData(String encryptedData, SecretKey secretKey) throws Exception {

        if (StringUtils.isBlank(encryptedData)) {
            return null;
        }

        Cipher cipher = Cipher.getInstance(DATA_INTEGRITY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedData));

    }

}
