package dev.youtiao.movie_booking.service;

import dev.youtiao.movie_booking.dao.MovieBookingOrderMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingOrder;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeatsExample;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.OrderDTO;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Service
public class OrderService {

    public static final String ORDER_STATUS_NOT_PAID = "not paid";

    public static final String ORDER_STATUS_PAID = "paid";

    public static final String ORDER_STATUS_CANCELED = "canceled";


    @Resource
    private MovieBookingSeatsMapper seatsMapper;

    @Resource
    private TheatreInfoMapper theatreInfoMapper;

    @Resource
    private MovieBookingOrderMapper orderMapper;

    @Resource
    private MovieBookingSystemPlayMapper playMapper;


    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public OrderDTO placeAnOrder(int playId, int seatNo) {
        MovieBookingSeatsExample seatsQuery = new MovieBookingSeatsExample();
        seatsQuery.createCriteria().andPlayIdEqualTo(playId).andSeatNoEqualTo(seatNo);
        boolean existsSeat = seatsMapper.countByExample(seatsQuery) > 0;
        if(!existsSeat) {
            MovieBookingSeats seat = new MovieBookingSeats();
            seat.setSeatNo(seatNo);
            seat.setPlayId(playId);
            int seatId = seatsMapper.insertSelective(seat);

            MovieBookingOrder order = new MovieBookingOrder();
            order.setPlayId(playId);
            order.setSeatId(seatId);
            order.setPayStatus(ORDER_STATUS_NOT_PAID);
            Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            order.setUserId(userId);
            int orderId = orderMapper.insertSelective(order);
            order.setOrderId(orderId);
            MovieBookingSystemPlay play = playMapper.selectByPrimaryKey(playId);
            playMapper.incrementSeatOccupied(playId);
            return createDTO(order, seat, play);
        } else {
            throw new IllegalStateException("Selected seat " + seatNo + " is already occupied, please try others.");
        }
    }

    public void payOrder(int orderId) {
        MovieBookingOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setPayStatus(ORDER_STATUS_PAID);
        orderMapper.updateByPrimaryKey(order);
    }

    private OrderDTO createDTO(MovieBookingOrder order, MovieBookingSeats seat, MovieBookingSystemPlay play) {
        TheatreInfo theatreInfo = theatreInfoMapper.selectByPrimaryKey(play.getTheatreId());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSeatNo(seat.getSeatNo());
        orderDTO.setTheatreName(theatreInfo.getTheatreName());
        orderDTO.setTheatreAddr(theatreInfo.getTheatreAddress());
        orderDTO.setPrice(play.getPrice());
        orderDTO.setPlayStart(format.format(play.getStartFrom()));
        orderDTO.setPlayEnd(format.format(play.getEndAt()));
        orderDTO.setHallNo(play.getHallNo());
        orderDTO.setPayStatus(order.getPayStatus());
        orderDTO.setOrderId(order.getOrderId());
        return orderDTO;
    }
}
