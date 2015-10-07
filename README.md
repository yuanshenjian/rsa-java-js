##Introduction
A demo project of **spring-boot**, which involves **rsa** *javascript encryption* and *java decryption*.


###Basic environment
####Back-end
* Java 8 
* Gradle 2.5 or above


####Front-end
* jsencrypt.js
* jQuery.js  


####Skills
* RSA
* spring-boot


###Reference
* Spring-boot : <http://projects.spring.io/spring-boot>
* JSEncrypt : <http://travistidwell.com/jsencrypt>
* JSEncrypt-Github : <https://github.com/travist/jsencrypt>
 

###Run application
####Test
* $ gradle clean test

####Start server 
#####Same domain
* $ gradle clean bootRun

#####Cors domain
* $ gradle clean build
* $ java -jar -Dserver.port=8099 build/libs/rsa-java-js-1.0-SNAPSHOT.jar
* $ java -jar -Dserver.port=8088 build/libs/rsa-java-js-1.0-SNAPSHOT.jar

###Contact me
* **Emali :** <sjyuan@thoughtworks.com>
* **ThoughtWorks:** <https://www.thoughtworks.com>

***


####Core source code
#####Dependencies
~~~java
dependencies {
    compile 'org.springframework.boot:spring-boot-starter-web'
    compile 'org.springframework.boot:spring-boot-starter-thymeleaf'
    compile 'org.springframework.boot:spring-boot-configuration-processor'

    compile 'com.alibaba:fastjson:1.1.34'
    compile 'org.codehaus.jackson:jackson-mapper-asl:1.9.13'
    compile 'org.codehaus.jackson:jackson-core-asl:1.9.13'
    compile 'com.jayway.jsonpath:json-path:0.9.1'
    compile "com.google.code.gson:gson:2.3.1"
    compile 'commons-codec:commons-codec:1.10'

    testCompile 'com.github.dreamhead:moco-core:0.10.0'
    testCompile 'org.springframework.boot:spring-boot-starter-test'
}
~~~

#####JS encryption
~~~html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Encrypt</title>
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}"/>
    <script type="text/javascript" th:src="@{/js/jquery.min.js}"></script>
    <script type="text/javascript" th:src="@{/js/jsencrypt.js}"></script>
</head>
<head>
</head>
<body>
<div class="container">
    <div class="navbar">
        <div class="navbar-inner">
            <a class="brand" href="http://www.thymeleaf.org"> Thymeleaf -
                Plain </a>
        </div>
    </div>
    <h1>Encrypt</h1>
    <input id="message"/>
    <div>
        <button id="getPublicKey">get public key</button>
    </div>

    <p id="publicKey"/>
    <div>
        <button id="encryption">encrypt</button>
    </div>
    <p id="encryptionResult"/>
    <div>
        <button id="decryption">decrypt</button>
    </div>
    <p id="decryptionResult"/>
</div>
<script>
	var publicKeyUrl = "http://localhost:8099/rjj/encryption-parameters";
    var encryptUrl = "http://localhost:8099/rjj/encryption-data";
    $("#getPublicKey").click(function (e) {
        e.preventDefault();
        $('#publicKey').text("loading...");
        $.ajax({
            url: publicKeyUrl,
            type: "GET",
            xhrFields: {
                withCredentials: true
            },
            success: function (result) {
                $('#publicKey').text(result["publicKey"]);
                if (result["publicKey"] != null && result["publicKey"] != "") {
                    $('#publicKey').text(result["publicKey"]);
                }
            },
            error: function () {
                alert("error");
            }
        });
    });

    $("#encryption").click(function (e) {
        e.preventDefault();
        var encrypt = new JSEncrypt();
        var publicKey = $('#publicKey').text();
        encrypt.setPublicKey(publicKey);
        var msg = $('#message').val();
        var value = {"cardNo": msg, "idType": "2"};
        var encrypted = encrypt.encrypt(JSON.stringify(value));
        console.log(encrypted);
        $("#encryptionResult").text(encrypted);
        $.ajax({
            type: "POST",
            url: encryptUrl,
            contentType: "application/json",
            data: JSON.stringify({"encryptedData": encrypted}),
            xhrFields: {
                withCredentials: true
            },
            success: function (result) {
                console.log("successful" + result);
            }
        });
    });
</script>
</body>
</html>
~~~


#####Java decryption
~~~java
@RestController
public class EncryptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionController.class);

    @RequestMapping(value = "/encryption-parameters",
                    method = RequestMethod.GET)
    public ResponseEntity<?> getEncryptionPublicKey(HttpServletRequest request) {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        request.getSession().setAttribute("_private_key", privateKey);
        
        Map<String, Object> publicKeyMap = new HashMap<>();
        publicKeyMap.put("publicKey", Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        return ResponseEntity.ok(publicKeyMap);
    }

    @RequestMapping(value = "/encryption-data",
                    method = RequestMethod.POST)
    public ResponseEntity<?> decrypt(HttpServletRequest request, @RequestBody String encryptedData) throws IOException {
        encryptedData = JSON.parseObject(encryptedData).getString("encryptedData");
        PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("_private_key");
        LOGGER.info("Decrypt data = {}", RSAUtils.decrypt(encryptedData, privateKey));
        return new ResponseEntity(HttpStatus.OK);
    }
}
~~~


####RSAUtils.java
~~~java
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    public static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    public static final int KEY_SIZE_2048 = 2048;
    public static final int KEY_SIZE_1024 = 1024;

    private RSAUtils() {
    }

    private static final String ALGORITHM = "RSA";

    public static KeyPair generateKeyPair() {
        return generateKeyPair(KEY_SIZE_2048);
    }

    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Failed to generate key pair!", e);
        }
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key!", e);
        }
    }

    public static PublicKey getPublicKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key!", e);
        }
    }

    public static String getBase64PublicKey(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public static String getBase64PrivateKey(PrivateKey privateKey) {
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

    public static byte[] encryptAsByteArray(String data, String base64PublicKey) {
        return encryptAsByteArray(data, getPublicKey(base64PublicKey));
    }

    public static String encryptAsString(String data, PublicKey publicKey) {
        return Base64.encodeBase64String(encryptAsByteArray(data, publicKey));
    }

    public static String encryptAsString(String data, String base64PublicKey) {
        return Base64.encodeBase64String(encryptAsByteArray(data, getPublicKey(base64PublicKey)));
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

    public static String decrypt(byte[] data, String base64PrivateKey) {
        return decrypt(data, getPrivateKey(base64PrivateKey));
    }

    public static String decrypt(String data, PrivateKey privateKey) {
        return decrypt(Base64.decodeBase64(data), privateKey);
    }

    public static String decrypt(String data, String base64PrivateKey) {
        return decrypt(Base64.decodeBase64(data), getPrivateKey(base64PrivateKey));
    }
}
~~~