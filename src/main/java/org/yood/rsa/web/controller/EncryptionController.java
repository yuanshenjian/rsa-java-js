package org.yood.rsa.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yood.rsa.util.RSAUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EncryptionController {

    public static PrivateKey glogalPprivateKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionController.class);

    @RequestMapping(value = "/encryption-parameters",
                    method = RequestMethod.GET)
    public ResponseEntity<?> getEncryptionPublicKey(HttpServletRequest request) {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        request.getSession().setAttribute("_private_key", privateKey);
        glogalPprivateKey = privateKey;
        String publicKeyString = RSAUtils.getPublicKeyStr(publicKey);
        LOGGER.info("modulus = {}, exponent = {}", publicKey.getModulus(), publicKey.getPublicExponent());
        LOGGER.info("Public key = {}", publicKeyString);
        Map<String, Object> publicKeyMap = new HashMap<>();
        publicKeyMap.put("publicKey", Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        return ResponseEntity.ok(publicKeyMap);
    }

    @RequestMapping(value = "/encryption-data",
                    method = RequestMethod.POST)
    public ResponseEntity<?> decrypt(HttpServletRequest request, @RequestBody String encryptedData) throws IOException {
        encryptedData = JSON.parseObject(encryptedData).getString("encryptedData");
        LOGGER.info("encrypt data = {}", encryptedData);
        PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("_private_key");
        LOGGER.info("Decrypt data = {}", RSAUtils.decrypt(encryptedData, privateKey));
        return new ResponseEntity(HttpStatus.OK);
    }
}
