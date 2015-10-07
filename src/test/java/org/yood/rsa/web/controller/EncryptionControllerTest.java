package org.yood.rsa.web.controller;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.yood.rsa.util.JSONUtils;
import org.yood.rsa.util.RSAUtils;

import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.security.PrivateKey;

import static junit.framework.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EncryptionControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionControllerTest.class);

    @InjectMocks
    private EncryptionController encryptionController;

    protected MockMvc mockMvc;

    @Before
    public void mvcSetup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(encryptionController).build();
    }

    @Test
    public void testGetEncryptionPublicKey() throws Exception {
        HttpSession session = mockMvc.perform(get("/encryption-parameters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicKey").exists())
                .andDo(print())
                .andReturn()
                .getRequest()
                .getSession();
        String privateKey = session.getAttribute("_private_key").toString();
        assertNotNull(privateKey);
    }

    @Test
    public void testDecrypt() throws Exception {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        LOGGER.info("public key = {}", Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("_private_key", privateKey);
        String encryptAsString = RSAUtils.encryptAsString("hello world", keyPair.getPublic());
        LOGGER.info("Encrypt data = {}", encryptAsString);
        ImmutableMap<String, String> content = ImmutableMap.of("encryptedData", encryptAsString);
        mockMvc.perform(post("/encryption-data").contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                                .content(JSONUtils.toJSONString(content))).andExpect(status().isOk());
    }
}