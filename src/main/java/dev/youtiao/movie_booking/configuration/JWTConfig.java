package dev.youtiao.movie_booking.configuration;

import dev.youtiao.movie_booking.sec.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class JWTConfig {
    @Value("${security.jwt.privkey}")
    private String JWTPrivKey;
    @Value("${security.jwt.pubkey}")
    private String JWTPubKey;
    @Bean
    public JWTUtils provideJWT() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new JWTUtils(JWTPrivKey, JWTPubKey);
    }
    }
