package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dto.RegistrationDTO;
import dev.youtiao.movie_booking.service.RegService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegServiceTest {
    @Mock
    private MovieBookingSystemUsersMapper mapper;

    @InjectMocks
    private RegService regService;

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
