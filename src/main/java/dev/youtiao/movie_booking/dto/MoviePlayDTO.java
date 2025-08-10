package dev.youtiao.movie_booking.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class MoviePlayDTO {
    private int playId;
    private String theatreAddr;

    private String theatreName;
    private BigDecimal theatreLon;
    private BigDecimal theatreLat;
    private String playStart;
    private String playEnd;
    private BigDecimal price;

    private Integer hallNo;
    private int seatsOccupied;
    private int seatsTotal;

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public String getTheatreAddr() {
        return theatreAddr;
    }

    public void setTheatreAddr(String theatreAddr) {
        this.theatreAddr = theatreAddr;
    }

    public BigDecimal getTheatreLon() {
        return theatreLon;
    }

    public void setTheatreLon(BigDecimal theatreLon) {
        this.theatreLon = theatreLon;
    }

    public BigDecimal getTheatreLat() {
        return theatreLat;
    }

    public void setTheatreLat(BigDecimal theatreLat) {
        this.theatreLat = theatreLat;
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

    public int getSeatsOccupied() {
        return seatsOccupied;
    }

    public void setSeatsOccupied(int seatsOccupied) {
        this.seatsOccupied = seatsOccupied;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public Integer getHallNo() {
        return hallNo;
    }

    public void setHallNo(Integer hallNo) {
        this.hallNo = hallNo;
    }
}
