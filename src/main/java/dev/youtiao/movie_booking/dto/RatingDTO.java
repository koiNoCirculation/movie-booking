package dev.youtiao.movie_booking.dto;

public class RatingDTO {
    private String imdbRating;
    private String auditorRating;

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getAuditorRating() {
        return auditorRating;
    }

    public void setAuditorRating(String auditorRating) {
        this.auditorRating = auditorRating;
    }
}
