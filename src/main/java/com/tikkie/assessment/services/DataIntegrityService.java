package com.tikkie.assessment.services;

import javax.crypto.SecretKey;

/**
 * @author sanket.dixit
 *
 * DataIntegrityService is the service interface for the Data Integrity Service
 */
public interface DataIntegrityService {

    /**
     * Encrypts the data using the secret key
     *
     * @param data      the data to be encrypted
     * @param secretKey the secret key to be used for encryption
     * @return the encrypted data
     * @throws Exception if any error occurs during encryption
     */
    String encryptData(String data, SecretKey secretKey) throws Exception;

    /**
     * Decrypts the encrypted data using the secret key
     *
     * @param encryptedData the encrypted data to be decrypted
     * @param secretKey     the secret key to be used for decryption
     * @return the decrypted data
     * @throws Exception if any error occurs during decryption
     */
    String decryptData(String encryptedData, SecretKey secretKey) throws Exception;

}
