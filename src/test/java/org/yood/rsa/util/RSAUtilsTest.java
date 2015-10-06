package org.yood.rsa.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RSAUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtilsTest.class);

    private static KeyPair keyPair;
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    @BeforeClass
    public static void setup() {
        keyPair = RSAUtils.generateKeyPair(RSAUtils.KEY_SIZE_2048);
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        LOGGER.info("Public Key = {}, Private Key = {}", publicKey, privateKey);
    }

    @Test
    public void testGenerateKeyPairWhenDefaultSize() throws Exception {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        assertNotNull(keyPair);
        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }

    @Test
    public void testGenerateKeyPair() throws Exception {
        KeyPair keyPair = RSAUtils.generateKeyPair(RSAUtils.KEY_SIZE_2048);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        assertNotNull(keyPair);
        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }

    @Test
    public void testGetBase64PublicKey() throws Exception {
        String publicKeyString = RSAUtils.getBase64PublicKey(publicKey);
        assertEquals(publicKey, RSAUtils.getPublicKey(publicKeyString));
    }

    @Test
    public void testGetPublicKey() throws Exception {
        testGetBase64PublicKey();
    }

    @Test
    public void testGetBase64PrivateKey() throws Exception {
        String privateKeyString = RSAUtils.getBase64PrivateKey(privateKey);
        assertEquals(privateKey, RSAUtils.getPrivateKey(privateKeyString));
    }

    @Test
    public void testGetPublicKeyFromNAndEBigInteger() throws Exception {
        BigInteger modulus = new BigInteger(publicKey.getEncoded());
        BigInteger exponent = new BigInteger("65573");
        assertNotNull(RSAUtils.getPublicKey(modulus, exponent));
    }

    @Test
    public void testGetPrivateKeyFromNAndEBigInteger() throws Exception {
        BigInteger modulus = new BigInteger(privateKey.getEncoded());
        BigInteger exponent = new BigInteger("65573");
        assertNotNull(RSAUtils.getPrivateKey(modulus, exponent));
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        testGetBase64PrivateKey();
    }

    @Test
    public void testEncryptAsByteArray() throws Exception {
        String encryptData = "中华人民共和国";
        LOGGER.info("Encrypt data = {}", encryptData);
        byte[] encryptedData = RSAUtils.encryptAsByteArray(encryptData, publicKey);
        String decryptData = RSAUtils.decrypt(encryptedData, privateKey);
        LOGGER.info("Decrypt data = {}", decryptData);
        assertEquals(encryptData, decryptData);
    }

    @Test
    public void testEncryptAsString() throws Exception {
        String encryptData = "中华人民共和国";
        LOGGER.info("Encrypt data = {}", encryptData);
        String encryptedData = RSAUtils.encryptAsString(encryptData, publicKey);
        String decryptData = RSAUtils.decrypt(encryptedData, privateKey);
        LOGGER.info("Decrypt data = {}", decryptData);
        assertEquals(encryptData, decryptData);
    }

    @Test
    public void testDecryptForByteArray() throws Exception {
        testEncryptAsByteArray();
    }

    @Test
    public void testDecryptForString() throws Exception {
        testEncryptAsString();
    }

    @Test
    public void testEncryptAsString1() throws Exception {
        String encryptData = "中华人民共和国";
        LOGGER.info("Encrypt data = {}", encryptData);
        String encryptedData = RSAUtils.encryptAsString(encryptData, RSAUtils.getBase64PublicKey(publicKey));
        String decryptData = RSAUtils.decrypt(encryptedData, RSAUtils.getBase64PrivateKey(privateKey));
        LOGGER.info("Decrypt data = {}", decryptData);
        assertEquals(encryptData, decryptData);
    }

    @Test
    public void testEncryptAsByteArray1() throws Exception {
        String encryptData = "中华人民共和国";
        LOGGER.info("Encrypt data = {}", encryptData);
        byte[] encryptedData = RSAUtils.encryptAsByteArray(encryptData, RSAUtils.getBase64PublicKey(publicKey));
        String decryptData = RSAUtils.decrypt(encryptedData, RSAUtils.getBase64PrivateKey(privateKey));
        LOGGER.info("Decrypt data = {}", decryptData);
        assertEquals(encryptData, decryptData);
    }
}