$(document).ready(function () {
    var publicKeyUrl = "http://localhost:8099/rjj/encryption-parameters";
    var encryptUrl = "http://localhost:8099/rjj/encryption-data";

    $("#getPublicKey").click(function (e) {
        e.preventDefault();

        $('#publicKey').text("loading...");
        $.get(publicKeyUrl, function (result) {
            $('#publicKey').text(result["publicKey"]);
            if (result["publicKey"] != null && result["publicKey"] != "") {
                $('#publicKey').text(result["publicKey"]);
            }
        })
            .fail(function () {
                alert("error");
            });

    });

    $("#encryption").click(function (e) {
        e.preventDefault();
        if ($("#message").val() != "") {
            var encrypt = new JSEncrypt();
            var publicKey = $('#publicKey').text();
            encrypt.setPublicKey(publicKey);
            var encrypted = encrypt.encrypt($('#message').val());
            console.log(encrypted);
            $("#encryptionResult").text(encrypted);
            $.ajax({
                type: "POST",
                url: encryptUrl,
                contentType: "application/json",
                data: JSON.stringify({"encryptedData": encrypted}),
                dataType: "json",
                success: function (result) {
                    console.log("successful" + result);
                }
            });
        }
    });

    $("#decryption").click(function (e) {
        e.preventDefault();
        var privateKey = "MIICXQIBAAKBgQDlOJu6TyygqxfWT7eLtGDwajtNFOb9I5XRb6khyfD1Yt3YiCgQWMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76xFxdU6jE0NQ+Z+zEdhUTooNRaY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4gwQco1KRMDSmXSMkDwIDAQABAoGAfY9LpnuWK5Bs50UVep5c93SJdUi82u7yMx4iHFMc/Z2hfenfYEzu+57fI4fvxTQ//5DbzRR/XKb8ulNv6+CHyPF31xk7YOBfkGI8qjLoq06V+FyBfDSwL8KbLyeHm7KUZnLNQbk8yGLzB3iYKkRHlmUanQGaNMIJziWOkN+N9dECQQD0ONYRNZeuM8zd8XJTSdcIX4a3gy3GGCJxOzv16XHxD03GW6UNLmfPwenKu+cdrQeaqEixrCejXdAFz/7+BSMpAkEA8EaSOeP5Xr3ZrbiKzi6TGMwHMvC7HdJxaBJbVRfApFrE0/mPwmP5rN7QwjrMY+0+AbXcm8mRQyQ1+IGEembsdwJBAN6az8Rv7QnD/YBvi52POIlRSSIMV7SwWvSK4WSMnGb1ZBbhgdg57DXaspcwHsFV7hByQ5BvMtIduHcT14ECfcECQATeaTgjFnqE/lQ22Rk0eGaYO80cc643BXVGafNfd9fcvwBMnk0iGX0XRsOozVt5AzilpsLBYuApa66NcVHJpCECQQDTjI2AQhFc1yRnCU/YgDnSpJVm1nASoRUnU8Jfm3Ozuku7JUXcVpt08DFSceCEX9unCuMcT72rAQlLpdZir876";
        var decrypt = new JSEncrypt();
        decrypt.setPrivateKey(privateKey);
        var decrypted = decrypt.decrypt($("#encryptionResult").text());
        console.log(decrypted);
        $("#decryptionResult").text(decrypted);
    });
});
