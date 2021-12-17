package server;

import com.google.gson.Gson;
import security.UserDetails;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

public class JWebToken {

    private static final String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String header_Base_64 = base64(header);
    private static final String secret_key = "sdfsdfsdf_sfsdaf933+-_asdfi";

    private final UserDetails details;
    private final String signature;
    private String payloadBase64;
    private static final Gson gson = new Gson();

    public JWebToken(UserDetails details){
        String payload = toJson(details);
        this.details = details;
        payloadBase64 = base64(payload);
        signature = hmacSha256(header_Base_64 + "." + payloadBase64, secret_key);
    }

    public JWebToken(String token){

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Token format");
        }

        if (!header_Base_64.equals(parts[0])) {
            throw new RuntimeException(
                    new NoSuchAlgorithmException("JWT Header is Incorrect: " + parts[0])
            );
        }

        payloadBase64 = parts[1];
        details = gson.fromJson(decodeBase64(payloadBase64), UserDetails.class);
        signature = parts[2];
    }

    public String toString(){
        return header_Base_64 + "." + payloadBase64 + "." + signature;
    }

    private String toJson(UserDetails details){
        return gson.toJson(details);
    }

    private static String base64(byte[] bytes){
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String base64(String text){
        return base64(text.getBytes());
    }

    private String decodeBase64(String encodedText){
        return new String(Base64.getUrlDecoder().decode(encodedText));
    }

    public boolean isValid() {
        return details != null && details.getExpires() > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                && signature.equals(hmacSha256(header_Base_64 + "." + payloadBase64, secret_key));
    }

    private String hmacSha256(String data, String secret){
        try {
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return base64(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails getDetails() {
        return details;
    }
}
