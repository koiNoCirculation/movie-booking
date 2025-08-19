package dev.youtiao.movie_booking.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.sec.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    JWTUtils jwtUtils;
    ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if(token == null) {
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken decode = null;
        try {
            decode = jwtUtils.decode(token);
        } catch (Exception e) {
            Response res = new Response.ResponseBuilder().setSucceed(false).setMessage(e.getMessage()).build();
            response.setStatus(403);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(decode);
        super.doFilterInternal(request, response, chain);
    }
}
