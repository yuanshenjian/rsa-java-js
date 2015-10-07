##Description  
A demo project of **spring-boot**, which involves **rsa** *javascript encryption* and *java decryption*.

###Basic environment
####Back-end
* Java 8 
* Gradle 2.5 or above

####Front-end
* jsencrypt.js
* jQuery.js  

***

####Core code
#####JS encryption
~~~javascritp
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


####Skills
* RSA
* spring-boot
 
###Reference
* Spring-boot : <http://projects.spring.io/spring-boot>
* jsencrypt: <https://github.com/travist/jsencrypt>
  
###Contact me
* **Emali :** <sjyuan@thoughtworks.com>
* **ThoughtWorks:** <https://www.thoughtworks.com>