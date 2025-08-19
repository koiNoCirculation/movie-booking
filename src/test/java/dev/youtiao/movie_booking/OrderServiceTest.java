package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingOrderMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemMoviesMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingOrder;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMovies;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.OrderDTO;
import dev.youtiao.movie_booking.service.OrderService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static dev.youtiao.movie_booking.service.OrderService.ORDER_STATUS_NOT_PAID;
import static dev.youtiao.movie_booking.service.OrderService.ORDER_STATUS_PAID;
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

    @Mock
    private MovieBookingSystemMoviesMapper moviesMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeAll
    public static void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(1);
        mockStatic(SecurityContextHolder.class).when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @Test
    public void testPlaceOrderWhenSeatAvail() {

        MovieBookingSystemMovies movie = new MovieBookingSystemMovies();
        movie.setMovieId(1);
        movie.setName("An awesome movie");
        movie.setCast("XXX");
        movie.setDirector("XXX");
        movie.setGenre("XXX");
        movie.setIntroduction("XXX");
        movie.setCoverImgUrl("XXX");
        movie.setLength(123);
        when(moviesMapper.selectByPrimaryKey(1)).thenReturn(movie);
        when(seatsMapper.countByExample(any())).thenReturn(0L);

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
        doAnswer((inv) -> {
            ((MovieBookingOrder) inv.getArguments()[0]).setOrderId(1);
            return null;
        }).when(orderMapper).insertSelective(any(MovieBookingOrder.class));
        doAnswer((inv) -> {
            ((MovieBookingSeats) inv.getArguments()[0]).setSeatId(1);
            return null;
        }).when(seatsMapper).insertSelective(any(MovieBookingSeats.class));
        OrderDTO orderDTO = orderService.placeAnOrder(1, 50);
        Assertions.assertEquals(1,orderDTO.getOrderId());
        Assertions.assertEquals(50, orderDTO.getSeatNo());
        Assertions.assertEquals(1,orderDTO.getHallNo());
        Assertions.assertEquals(ORDER_STATUS_NOT_PAID, orderDTO.getPayStatus());
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

    @Test
    public void testPayOrder() {
        MovieBookingOrder movieBookingOrder = new MovieBookingOrder();
        movieBookingOrder.setOrderId(1);
        when(orderMapper.selectByPrimaryKey(1)).thenReturn(movieBookingOrder);
        orderService.payOrder(1);
        verify(orderMapper, times(1)).updateByPrimaryKey(any(MovieBookingOrder.class));
    }

    @Test
    public void testCancelOrder() {
        when(orderMapper.selectByExample(any())).thenReturn(Collections.EMPTY_LIST);
        Assertions.assertFalse(orderService.cancelOrder(1));
        verify(orderMapper, times(0)).updateByPrimaryKeySelective(any());

        when(orderMapper.selectByExample(any())).thenReturn(Arrays.asList(new MovieBookingOrder()));
        Assertions.assertTrue(orderService.cancelOrder(1));
        verify(orderMapper, times(1)).updateByPrimaryKeySelective(any());
    }

    @Test
    public void testGetMyOrders() {
        MovieBookingOrder order1 = new MovieBookingOrder();
        order1.setOrderId(1);
        order1.setSeatId(1);
        order1.setPlayId(1);
        order1.setPayStatus(ORDER_STATUS_NOT_PAID);
        MovieBookingOrder order2 = new MovieBookingOrder();
        order2.setOrderId(2);
        order2.setSeatId(2);
        order2.setPlayId(2);
        order2.setPayStatus(ORDER_STATUS_PAID);
        when(orderMapper.selectByExample(any())).thenReturn(
                Arrays.asList(
                        order1,
                        order2
                )
        );
        MovieBookingSystemPlay play1 = new MovieBookingSystemPlay();
        play1.setPlayId(1);
        play1.setStartFrom(new DateTime(2025,8,1,12,0,0).toDate());
        play1.setEndAt(new DateTime(2025,8,1,15,0,0).toDate());
        play1.setPrice(new BigDecimal(25));
        play1.setMovieId(1);
        play1.setTheatreId(1);
        play1.setHallNo(1);
        MovieBookingSystemPlay play2 = new MovieBookingSystemPlay();
        play2.setPlayId(2);
        play2.setStartFrom(new DateTime(2025,8,2,12,0,0).toDate());
        play2.setEndAt(new DateTime(2025,8,2,15,0,0).toDate());
        play2.setPrice(new BigDecimal(52));
        play2.setMovieId(2);
        play2.setTheatreId(2);
        play2.setHallNo(2);
        when(playMapper.selectByExample(any())).thenReturn(Arrays.asList(play1, play2));

        MovieBookingSeats seat1 = new MovieBookingSeats();
        seat1.setSeatId(1);
        seat1.setSeatNo(1);
        seat1.setPlayId(1);
        MovieBookingSeats seat2 = new MovieBookingSeats();
        seat2.setSeatId(2);
        seat2.setSeatNo(2);
        seat2.setPlayId(2);

        when(seatsMapper.selectByExample(any())).thenReturn(Arrays.asList(seat1, seat2));

        TheatreInfo theatreInfo1 = new TheatreInfo();
        theatreInfo1.setTheatreId(1);
        theatreInfo1.setTheatreName("A");
        theatreInfo1.setTheatreAddress("AA");
        when(theatreInfoMapper.selectByPrimaryKey(1)).thenReturn(theatreInfo1);
        TheatreInfo theatreInfo2 = new TheatreInfo();
        theatreInfo2.setTheatreId(2);
        theatreInfo2.setTheatreName("B");
        theatreInfo2.setTheatreAddress("BB");
        when(theatreInfoMapper.selectByPrimaryKey(2)).thenReturn(theatreInfo2);


        MovieBookingSystemMovies movie1 = new MovieBookingSystemMovies();
        movie1.setName("Mov1");
        movie1.setCoverImgUrl("url1");
        when(moviesMapper.selectByPrimaryKey(1)).thenReturn(movie1);
        MovieBookingSystemMovies movie2 = new MovieBookingSystemMovies();
        movie2.setName("Mov2");
        movie2.setCoverImgUrl("url2");
        when(moviesMapper.selectByPrimaryKey(2)).thenReturn(movie2);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        OrderDTO dto1 = new OrderDTO();
        dto1.setOrderId(1);
        dto1.setMovieName("Mov1");
        dto1.setCoverImageURL("url1");
        dto1.setPlayStart(format.format(new DateTime(2025,8,1,12,0,0).toDate()));
        dto1.setPlayEnd(format.format(new DateTime(2025,8,1,15,0,0).toDate()));
        dto1.setPrice(new BigDecimal(25));
        dto1.setTheatreName("A");
        dto1.setTheatreAddr("AA");
        dto1.setHallNo(1);
        dto1.setSeatNo(1);
        dto1.setPayStatus(ORDER_STATUS_NOT_PAID);
        OrderDTO dto2 = new OrderDTO();
        dto2.setOrderId(2);
        dto2.setMovieName("Mov2");
        dto2.setCoverImageURL("url2");
        dto2.setPlayStart(format.format(new DateTime(2025,8,2,12,0,0).toDate()));
        dto2.setPlayEnd(format.format(new DateTime(2025,8,2,15,0,0).toDate()));
        dto2.setPrice(new BigDecimal(52));
        dto2.setTheatreName("B");
        dto2.setTheatreAddr("BB");
        dto2.setHallNo(2);
        dto2.setSeatNo(2);
        dto2.setPayStatus(ORDER_STATUS_PAID);

        List<OrderDTO> myOrders = orderService.getMyOrders(1, 5);
        Assertions.assertEquals(2, myOrders.size());
        Assertions.assertEquals(dto1, myOrders.get(0));
        Assertions.assertEquals(dto2, myOrders.get(1));
    }
}
