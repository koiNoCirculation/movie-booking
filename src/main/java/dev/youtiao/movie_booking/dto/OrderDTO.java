package dev.youtiao.movie_booking.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderDTO {
    /*
       order_id integer,
       user_id integer,
       play_id integer,
       seat_id integer,
       pay_status varchar(32),
     */
    private Integer orderId;

    private String movieName;

    private String coverImageURL;

    private String payStatus;

    private Integer seatNo;

    private String theatreAddr;

    private String theatreName;

    private String playStart;

    private String playEnd;

    private BigDecimal price;

    private Integer hallNo;

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

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO other = (OrderDTO) o;
        return Objects.equals(this.orderId, other.orderId)
                && Objects.equals(this.movieName, other.movieName)
                && Objects.equals(this.coverImageURL, other.coverImageURL)
                && Objects.equals(this.payStatus, other.payStatus)
                && Objects.equals(this.seatNo, other.seatNo)
                && Objects.equals(this.theatreAddr, other.theatreAddr)
                && Objects.equals(this.theatreName, other.theatreName)
                && Objects.equals(this.playStart, other.playStart)
                && Objects.equals(this.playEnd, other.playEnd)
                && Objects.equals(this.price, other.price)
                && Objects.equals(this.hallNo, other.hallNo);
    }

    @Override
    public String toString() {
        return String.format("OrderDTO[%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s]",
                orderId, movieName, coverImageURL, payStatus,seatNo,theatreAddr,
                theatreName, playStart, playEnd, price, hallNo);
    }
}
