package dev.youtiao.movie_booking.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dto.MovieSystemUser;
import dev.youtiao.movie_booking.dto.MovieSystemUserDTO;
import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.sec.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.StringJoiner;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    JWTUtils jwtUtils;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MovieSystemUser principal = (MovieSystemUser) authResult.getPrincipal();
        DateTime plus1d = new DateTime().plusDays(1);
        try {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (GrantedAuthority authority : principal.getAuthorities()) {
                stringJoiner.add(authority.toString());
            }
            String token = jwtUtils.issue(principal.getUserId(), principal.getUsername(), stringJoiner.toString(), plus1d.toDate());
            response.setHeader("token", token);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            MovieSystemUserDTO movieSystemUserDTO = new MovieSystemUserDTO();
            movieSystemUserDTO.setUserId(principal.getUserId());
            movieSystemUserDTO.setDisplayName(principal.getDisplayName());
            movieSystemUserDTO.setUsername(principal.getUsername());
            Response.ResponseBuilder builder = new Response.ResponseBuilder();
            builder.setSucceed(true);
            builder.setData(movieSystemUserDTO);
            response.getWriter().write(objectMapper.writeValueAsString(builder.build()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String resp = String.format("{\"succeed\":false, \"message\":\"%s\"}", failed.getClass().getName() + ":" + failed.getMessage());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(resp);
    }
}
