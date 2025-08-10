package dev.youtiao.movie_booking.configuration;

import dev.youtiao.movie_booking.sec.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebMvcSecurityConfig {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain provideFilterChain(HttpSecurity httpSecurity) throws Exception {
        //free

        // /api/movieList
        // /api/getMovieDetail
        // /api/register
        // /login

        // authorized users
        // /api/chooseSeat
        // /api/performPayment
        // /api/getMyOrders
        // /api/cancelOrder
        // /api/checkIn

        // /api/manage/addMovie
        // /api/manage/deleteMovie
        // /api/manage/updateMovie

        // /api/manage/addThertre
        // /api/manage/deleteThertre
        // /api/manage/UpdateThertre

        // /api/manage/addMoviePlay
        // /api/manage/updateMoviePlay


        return httpSecurity.
                csrf(csrf -> csrf.disable()).
                authorizeHttpRequests(
                        (httpRequest) ->
                                httpRequest.requestMatchers("/api/chooseSeat",
                                                "/api/placeOrder",
                                                "/api/performPayment",
                                                "/api/getMyOrders",
                                                "/api/cancelOrder",
                                                "/api/checkIn").hasAnyRole("USER","ADMIN")
                                        .requestMatchers("/api/manage/addMovie",
                                                "/api/manage/deleteMovie",
                                                "/api/manage/updateMovie",
                                                "/api/manage/addThertre",
                                                "/api/manage/deleteThertre",
                                                "/api/manage/UpdateThertre",
                                                "/api/manage/addMoviePlay",
                                                "/api/manage/updateMoviePlay").hasRole("ADMIN")
                                        .requestMatchers("/home","/","/assets/**","/login", "/api/movieList", "/api/getSeats", "/api/movieRating","/api/getMoviePlays","/api/register").permitAll()
                                        .anyRequest().anonymous()
        ).logout(logout -> logout.permitAll())
                .addFilter(new JWTAuthorizationFilter(authenticationManager,jwtUtils))
                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtils))
                .build();
    }
}
