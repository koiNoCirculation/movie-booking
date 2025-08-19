package dev.youtiao.movie_booking.dto;

import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMovies;

import java.util.Objects;

public class MovieDTO {
    private Integer movieId;
    private String name;
    private Integer length;
    private String coverImageURL;
    private String director;
    private String introduction;

    private String cast;

    private String genre;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO that = (MovieDTO) o;
        return Objects.equals(movieId, that.movieId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(length, that.length) &&
                Objects.equals(director, that.director) &&
                Objects.equals(coverImageURL, that.coverImageURL) &&
                Objects.equals(cast, that.cast) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(introduction, that.introduction);
    }
    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s, %s, %s, %s, %s]",
                movieId, name, length, director, coverImageURL, cast, genre, introduction);
    }
}
