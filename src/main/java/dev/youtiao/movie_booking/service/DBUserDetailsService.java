package dev.youtiao.movie_booking.service;

import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsers;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsersExample;
import dev.youtiao.movie_booking.dto.MovieSystemUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBUserDetailsService implements UserDetailsService {
    @Resource
    private MovieBookingSystemUsersMapper mapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MovieBookingSystemUsersExample movieBookingSystemUsersExample = new MovieBookingSystemUsersExample();
        movieBookingSystemUsersExample.createCriteria().andNameEqualTo(username);
        List<MovieBookingSystemUsers> movieBookingSystemUsers = mapper.selectByExample(movieBookingSystemUsersExample);
        if(!movieBookingSystemUsers.isEmpty()) {
            MovieBookingSystemUsers selectedUser = movieBookingSystemUsers.get(0);
            String roles = selectedUser.getRole();
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (String r : roles.split(",")) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()));
            }
            MovieSystemUser user = new MovieSystemUser(selectedUser.getName(), selectedUser.getHashedPasswd(), selectedUser.getUserId(), selectedUser.getDisplayName(), grantedAuthorities);
            return user;
        }
        return null;
    }
}
