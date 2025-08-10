package dev.youtiao.movie_booking.dto;

import java.math.BigDecimal;

public class OrderDTO {
    /*
       order_id integer,
       user_id integer,
       play_id integer,
       seat_id integer,
       pay_status varchar(32),
     */
    private Integer orderId;

    private Integer seatNo;

    private String theatreAddr;

    private String theatreName;

    private String playStart;

    private String playEnd;

    private BigDecimal price;

    private Integer hallNo;

    private String payStatus;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTheatreAddr() {
        return theatreAddr;
    }

    public void setTheatreAddr(String theatreAddr) {
        this.theatreAddr = theatreAddr;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getPlayStart() {
        return playStart;
    }

    public void setPlayStart(String playStart) {
        this.playStart = playStart;
    }

    public String getPlayEnd() {
        return playEnd;
    }

    public void setPlayEnd(String playEnd) {
        this.playEnd = playEnd;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getHallNo() {
        return hallNo;
    }

    public void setHallNo(Integer hallNo) {
        this.hallNo = hallNo;
    }

    public Integer getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
