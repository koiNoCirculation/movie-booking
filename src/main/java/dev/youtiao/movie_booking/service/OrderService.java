package dev.youtiao.movie_booking.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import dev.youtiao.movie_booking.dao.MovieBookingOrderMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemMoviesMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingOrder;
import dev.youtiao.movie_booking.dao.entities.MovieBookingOrderExample;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeatsExample;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMovies;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlayExample;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.OrderDTO;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    public static final String ORDER_STATUS_NOT_PAID = "not paid";

    public static final String ORDER_STATUS_PAID = "paid";

    public static final String ORDER_STATUS_CANCELED = "canceled";


    @Resource
    private MovieBookingSeatsMapper seatsMapper;

    @Resource
    private MovieBookingSystemMoviesMapper moviesMapper;

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
            seatsMapper.insertSelective(seat);
            int seatId = seat.getSeatId();
            MovieBookingOrder order = new MovieBookingOrder();
            order.setPlayId(playId);
            order.setSeatId(seatId);
            order.setPayStatus(ORDER_STATUS_NOT_PAID);
            Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            order.setUserId(userId);
            orderMapper.insertSelective(order);
            Integer orderId = order.getOrderId();
            order.setOrderId(orderId);
            MovieBookingSystemPlay play = playMapper.selectByPrimaryKey(playId);
            MovieBookingSeatsExample countSeats = new MovieBookingSeatsExample();
            countSeats.createCriteria().andPlayIdEqualTo(playId);
            play.setSeatsOccupied((int) seatsMapper.countByExample(countSeats));
            playMapper.updateByPrimaryKeySelective(play);
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
        MovieBookingSystemMovies movie = moviesMapper.selectByPrimaryKey(play.getMovieId());
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
        orderDTO.setMovieName(movie.getName());
        orderDTO.setCoverImageURL(movie.getCoverImgUrl());
        orderDTO.setPayStatus(order.getPayStatus());
        return orderDTO;
    }

    public boolean cancelOrder(int orderId) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MovieBookingOrderExample criteria = new MovieBookingOrderExample();
        criteria.createCriteria().andOrderIdEqualTo(orderId).andUserIdEqualTo(userId);
        List<MovieBookingOrder> movieBookingOrders = orderMapper.selectByExample(criteria);
        if(movieBookingOrders.size() == 1) {
            MovieBookingOrder movieBookingOrder = movieBookingOrders.get(0);
            movieBookingOrder.setPayStatus(ORDER_STATUS_CANCELED);
            orderMapper.updateByPrimaryKeySelective(movieBookingOrder);
            return true;
        } else {
            return false;
        }
    }

    public List<OrderDTO> getMyOrders(int page, int size) {
        try(Page<Object> p = PageHelper.startPage(page, size)) {
            Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MovieBookingOrderExample criteria = new MovieBookingOrderExample();
            criteria.createCriteria().andUserIdEqualTo(userId);
            criteria.setOrderByClause("create_time desc");
            List<MovieBookingOrder> orders = orderMapper.selectByExample(criteria);
            if(orders.isEmpty()) {
                return Collections.emptyList();
            }
            List<Integer> seats = orders.stream().map(MovieBookingOrder::getSeatId).toList();
            List<Integer> plays = orders.stream().map(MovieBookingOrder::getPlayId).toList();
            MovieBookingSystemPlayExample criteriaPlay = new MovieBookingSystemPlayExample();
            criteriaPlay.createCriteria().andPlayIdIn(plays);
            MovieBookingSeatsExample criteriaSeats= new MovieBookingSeatsExample();
            criteriaSeats.createCriteria().andSeatIdIn(seats);
            Map<Integer, MovieBookingSystemPlay> playMap = playMapper.selectByExample(criteriaPlay).stream().
                    collect(Collectors.toMap(MovieBookingSystemPlay::getPlayId, e -> e));
            List<MovieBookingSeats> movieBookingSeats = seatsMapper.selectByExample(criteriaSeats);
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (int i = 0; i < orders.size(); i++) {
                orderDTOs.add(createDTO(orders.get(i), movieBookingSeats.get(i), playMap.get(orders.get(i).getPlayId())));
            }
            return orderDTOs;
        }
    }
}
