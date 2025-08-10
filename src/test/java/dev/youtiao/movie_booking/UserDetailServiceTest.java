package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsers;
import dev.youtiao.movie_booking.service.DBUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {
    @Mock
    private MovieBookingSystemUsersMapper usersMapper;

    @InjectMocks
    private DBUserDetailsService userDetailsService;
    @Test
    public void testUserDetailService() {
        MovieBookingSystemUsers user = new MovieBookingSystemUsers();
        user.setUserId(1);
        user.setHashedPasswd("aaa");
        user.setName("u");
        user.setRole("ADMIN,USER");
        user.setDisplayName("a");
        when(usersMapper.selectByExample(any())).thenReturn(Arrays.asList(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername("u");
        Assertions.assertEquals("u", userDetails.getUsername());
        Assertions.assertEquals("aaa", userDetails.getPassword());
        Assertions.assertTrue( userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Assertions.assertTrue( userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

    }
}
