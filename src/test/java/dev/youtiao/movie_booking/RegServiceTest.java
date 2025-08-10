package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingOrderMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsers;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsersExample;
import dev.youtiao.movie_booking.dto.RegistrationDTO;
import dev.youtiao.movie_booking.service.RegService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = RegServiceTest.ContextConfiguration.class)
public class RegServiceTest {
    @Autowired
    private MovieBookingSystemUsersMapper mapper;

    @Autowired
    private RegService regService;

    @Configuration
    @ComponentScan("dev.youtiao.movie_booking.service")
    public static class ContextConfiguration {
        @Bean
        public MovieBookingSystemUsersMapper mapper() {
            return mock(MovieBookingSystemUsersMapper.class);
        }
    }

    @Test
    public void testRegService() {
        when(mapper.countByExample(any())).thenReturn(0L);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("123");
        registrationDTO.setRepeatedpassword("123");
        Assertions.assertTrue(regService.register(registrationDTO).isSucceed());
        verify(mapper, atLeast(1)).insertSelective(any());
    }

    @Test
    public void testRegServiceFail4() {
        when(mapper.countByExample(any())).thenReturn(1L);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("123");
        registrationDTO.setRepeatedpassword("123");
        Assertions.assertFalse(regService.register(registrationDTO).isSucceed());
        verify(mapper, atLeast(0)).insertSelective(any());
    }

    @Test
    public void testRegServiceFail1() {
        when(mapper.countByExample(any())).thenReturn(0L);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("123");
        registrationDTO.setRepeatedpassword("456");
        Assertions.assertFalse(regService.register(registrationDTO).isSucceed());
        verify(mapper, atLeast(0)).insertSelective(any());
    }

    @Test
    public void testRegServiceFail2() {
        when(mapper.countByExample(any())).thenReturn(0L);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("");
        registrationDTO.setRepeatedpassword("");
        Assertions.assertFalse(regService.register(registrationDTO).isSucceed());
        verify(mapper, atLeast(0)).insertSelective(any());
    }

    @Test
    public void testRegServiceFail3() {
        when(mapper.countByExample(any())).thenReturn(0L);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("");
        registrationDTO.setRepeatedpassword("");
        Assertions.assertFalse(regService.register(registrationDTO).isSucceed());
        verify(mapper, atLeast(0)).insertSelective(any());
    }
}
