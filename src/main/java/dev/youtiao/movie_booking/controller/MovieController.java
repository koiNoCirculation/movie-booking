package dev.youtiao.movie_booking.controller;

import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.service.MovieService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    private Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Resource
    private MovieService movieService;

    @GetMapping("/api/movieList")
    public Response getMovieList(@RequestParam int page, @RequestParam int size) {
        logger.info("Retriving movie list, page = {}, pagesize = {}", page, size);
        return movieService.getMovies(page, size);
    }

    @GetMapping("/api/movieRating")
    public Response getRating(@RequestParam int movieId) {
        logger.info("Retriving movie rating, id = {}", movieId);
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        builder.setSucceed(true);
        builder.setData(movieService.getRatings(movieId));
        return builder.build();
    }

    @GetMapping("/api/getMoviePlays")
    public Response getMoviePlays(@RequestParam int movieId) {
        logger.info("Retriving movie plays, id = {}", movieId);
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        builder.setSucceed(true);
        builder.setData(movieService.getMoviePlays(movieId));
        return builder.build();
    }

    @GetMapping("/api/getSeats")
    public Response getSeats(@RequestParam int playId) {
        logger.info("Retriving movie plays, id = {}", playId);
        boolean[] occupations = movieService.getOccupations(playId);
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        builder.setSucceed(true);
        builder.setData(occupations);
        return builder.build();
    }
}
