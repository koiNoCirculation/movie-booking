package dev.youtiao.movie_booking.controller;

import dev.youtiao.movie_booking.dto.OrderDTO;
import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.service.OrderService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;
    @PostMapping("/api/placeOrder")
    public Response placeOrder(@RequestParam int playId, @RequestParam int seatNo) {
        /*
         a dummy thing, nothing actual is done in payment
         */
        logger.info("User id {} is placing an order, on play #{}", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), playId);
        OrderDTO orderDTO = orderService.placeAnOrder(playId, seatNo);
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        builder.setSucceed(true);
        builder.setData(orderDTO);
        return builder.build();
    }

    @PostMapping("/api/payOrder")
    public Response payOrder(@RequestParam int orderId) {
        /*a dummy pay :(*/
        logger.info("User id {} is paying for order, on order id {}",  SecurityContextHolder.getContext().getAuthentication().getPrincipal(), orderId);
        orderService.payOrder(orderId);
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        builder.setSucceed(true);
        builder.setMessage("Only dummy pay");
        return builder.build();
    }

    @GetMapping("/api/myOrders")
    public Response getMyOrders(@RequestParam int page, @RequestParam int size) {
        return null;
    }

    @GetMapping("/api/cancelOrder")
    public Response cancelOrder(@RequestParam int orderId) {
        return null;
    }
}
