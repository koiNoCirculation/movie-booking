package dev.youtiao.movie_booking.configuration;

import dev.youtiao.movie_booking.sec.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

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
                csrf(AbstractHttpConfigurer::disable).
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
        ).logout(LogoutConfigurer::permitAll)
                .addFilter(new JWTAuthorizationFilter(authenticationManager,jwtUtils))
                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtils))
                .build();
    }
}
