package org.yood.springboot.mybatis.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    public static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    private RSAUtils() {
    }

    private static final String ALGORITHM = "RSA";

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Failed to generate key pair!", e);
        }
    }

    public static PublicKey getPublicKey(String publicKeyStr) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key!", e);
        }
    }

    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public static PrivateKey getPrivateKey(String privateKeyStr) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public static PrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }


    public static byte[] encryptAsByteArray(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException("Encrypt failed!", e);
        }
    }

    public static byte[] encryptAsByteArray(String data, String publicKeyStr) {
        return encryptAsByteArray(data, getPublicKey(publicKeyStr));
    }

    public static String encryptAsString(String data, PublicKey publicKey) {
        return Base64.encodeBase64String(encryptAsByteArray(data, publicKey));
    }

    public static String encryptAsString(String data, String publicKeyStr) {
        return Base64.encodeBase64String(encryptAsByteArray(data, getPublicKey(publicKeyStr)));
    }


    public static String decrypt(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            throw new IllegalArgumentException("Decrypt failed!", e);
        }
    }

    public static String decrypt(byte[] data, String privateKeyStr) {
        return decrypt(data, getPrivateKey(privateKeyStr));
    }

    public static String decrypt(String data, PrivateKey privateKey) {
        return decrypt(Base64.decodeBase64(data), privateKey);
    }

    public static String decrypt(String data, String privateKeyStr) {
        return decrypt(Base64.decodeBase64(data), getPrivateKey(privateKeyStr));
    }
}

