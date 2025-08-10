package dev.youtiao.movie_booking.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.youtiao.movie_booking.sec.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    JWTUtils jwtUtils;

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
        SecurityContextHolder.getContext().setAuthentication(jwtUtils.decode(token));
        super.doFilterInternal(request, response, chain);
    }
}
