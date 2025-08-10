package dev.youtiao.movie_booking.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JWTUtils {
    private final Algorithm algorithm;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTUtils(String pri, String pub) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory rsa = KeyFactory.getInstance("RSA");
        KeySpec privspec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pri));
        KeySpec pubspec = new X509EncodedKeySpec(Base64.getDecoder().decode(pub));
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsa.generatePublic(pubspec);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) rsa.generatePrivate(privspec);
        this.algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }
    public DecodedJWT decode(String token, String username) {
        JWTVerifier verifier = JWT.require(algorithm)
                // specify any specific claim validations
                .withIssuer(username)
                // reusable verifier instance
                .build();

         return verifier.verify(token);
    }
    public UsernamePasswordAuthenticationToken decode(String token) throws JsonProcessingException {
        JWTVerifier verifier = JWT.require(algorithm)
                // specify any specific claim validations
                // reusable verifier instance
                .build();

        DecodedJWT verify = verifier.verify(token);
        String decodedPayloadString = new String(Base64.getDecoder().decode(verify.getPayload()), StandardCharsets.UTF_8);
        Map<String, Object> payloadMap = objectMapper.readValue(decodedPayloadString, new TypeReference<>() {});
        //String iss = (String) payloadMap.get("iss");
        Integer exp = (Integer) payloadMap.get("exp");
        String privileges = (String) payloadMap.get("privileges");
        Integer userId = (Integer) payloadMap.get("userId");
        if(DateTime.now().isAfter(new DateTime(exp * 1000L))) {
            throw new BadCredentialsException("The token has expired");
        }
        String[] split = privileges.split(",");
        List<GrantedAuthority> privilegeList = new ArrayList<>();
        for(String privilege: split) {
            privilegeList.add(new SimpleGrantedAuthority(privilege));
        }
        return new UsernamePasswordAuthenticationToken(userId, null, privilegeList);
    }
    public String issue(Integer userId, String username, String privileges, Date exipres) throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException {
        return JWT.create()
                .withClaim("userId", userId)
                .withIssuer(username)
                .withExpiresAt(exipres)
                .withClaim("privileges", privileges)
                .sign(algorithm);
    }
}
