package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingOrderMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.OrderDTO;
import dev.youtiao.movie_booking.service.OrderService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MovieBookingSeatsMapper seatsMapper;

    @Mock
    private TheatreInfoMapper theatreInfoMapper;

    @Mock
    private MovieBookingOrderMapper orderMapper;

    @Mock
    private MovieBookingSystemPlayMapper playMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testPlaceOrderWhenSeatAvail() {
        when(seatsMapper.countByExample(any())).thenReturn(0L);
        when(seatsMapper.insertSelective(any())).thenReturn(1);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(1);
        mockStatic(SecurityContextHolder.class).when(SecurityContextHolder::getContext).thenReturn(securityContext);
        MovieBookingSystemPlay play = new MovieBookingSystemPlay();
        play.setPlayId(1);
        play.setStartFrom(new DateTime(2025,8,1,12,0,0).toDate());
        play.setEndAt(new DateTime(2025,8,1,15,0,0).toDate());
        play.setPrice(new BigDecimal(25));
        play.setMovieId(1);
        play.setTheatreId(1);
        play.setSeatsOccupied(1);
        play.setSeatsTotal(100);
        play.setHallNo(1);
        when(playMapper.selectByPrimaryKey(any())).thenReturn(play);
        TheatreInfo theatreInfo = new TheatreInfo();
        theatreInfo.setTheatreId(1);
        theatreInfo.setTheatreName("any theatre");
        theatreInfo.setTheatreAddress("abc");
        when(theatreInfoMapper.selectByPrimaryKey(any())).thenReturn(theatreInfo);
        when(orderMapper.insertSelective(any())).thenReturn(1);
        OrderDTO orderDTO = orderService.placeAnOrder(1, 50);
        Assertions.assertEquals(1,orderDTO.getOrderId());
        Assertions.assertEquals(50, orderDTO.getSeatNo());
        Assertions.assertEquals(1,orderDTO.getHallNo());
        Assertions.assertEquals(OrderService.ORDER_STATUS_NOT_PAID, orderDTO.getPayStatus());
        Assertions.assertEquals(new BigDecimal(25), orderDTO.getPrice());
        Assertions.assertEquals("2025-08-01 12:00:00", orderDTO.getPlayStart());
        Assertions.assertEquals("2025-08-01 15:00:00", orderDTO.getPlayEnd() );
        Assertions.assertEquals("abc", orderDTO.getTheatreAddr());
        Assertions.assertEquals("any theatre", orderDTO.getTheatreName());
    }

    @Test
    public void testPlaceOrderWhenSeatNotAvail() {
        when(seatsMapper.countByExample(any())).thenReturn(1L);
        Assertions.assertThrows(IllegalStateException.class, () -> orderService.placeAnOrder(1,50));
    }
}
