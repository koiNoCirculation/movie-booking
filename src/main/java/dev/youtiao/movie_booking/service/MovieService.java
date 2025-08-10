package dev.youtiao.movie_booking.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemMoviesMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.RatingMovieMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeatsExample;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMovies;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMoviesExample;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlayExample;
import dev.youtiao.movie_booking.dao.entities.RatingMovie;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.MovieDTO;
import dev.youtiao.movie_booking.dto.MoviePlayDTO;
import dev.youtiao.movie_booking.dto.RatingDTO;
import dev.youtiao.movie_booking.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieService {
    @Resource
    private MovieBookingSystemMoviesMapper mapper;

    @Resource
    private RatingMovieMapper ratingMapper;

    @Resource
    private MovieBookingSystemPlayMapper playMapper;

    @Resource
    private TheatreInfoMapper theatreInfoMapper;

    @Resource
    private MovieBookingSeatsMapper seatsMapper;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public Response getMovies(int page, int pageSize) {
        try(Page<Object> p = PageHelper.startPage(page, pageSize)) {
            List<MovieBookingSystemMovies> movieBookingSystemMovies = mapper.selectByExampleWithBLOBs(new MovieBookingSystemMoviesExample());
            List<MovieDTO> movieDTOS = movieBookingSystemMovies.stream().map(e -> {
                MovieDTO movieDTO = new MovieDTO();
                movieDTO.setMovieId(e.getMovieId());
                movieDTO.setDirector(e.getDirector());
                movieDTO.setIntroduction(e.getIntroduction());
                movieDTO.setLength(e.getLength());
                movieDTO.setCoverImageURL(e.getCoverImgUrl());
                movieDTO.setMovieId(e.getMovieId());
                movieDTO.setIntroduction(e.getIntroduction());
                movieDTO.setGenre(e.getGenre());
                movieDTO.setCast(e.getCast());
                return movieDTO;
            }).collect(Collectors.toList());
            Response.ResponseBuilder builder = new Response.ResponseBuilder();
            builder.setSucceed(true);
            builder.setData(movieDTOS);
            return builder.build();
        }
    }

    public RatingDTO getRatings(int movieId) {
        RatingMovie ratingMovie = ratingMapper.selectByPrimaryKey(movieId);
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setImdbRating(Double.toString(ratingMovie.getImdbRating().setScale(1, RoundingMode.HALF_UP).doubleValue()));
        BigDecimal finalAuditRating = ratingMovie.getFinalAuditRating();
        if(finalAuditRating.doubleValue() > 0) {
            ratingDTO.setAuditorRating(Double.toString(finalAuditRating.setScale(1, RoundingMode.HALF_UP).doubleValue()));
        } else {
            ratingDTO.setAuditorRating("N/A");
        }
        return ratingDTO;
    }

    public List<MoviePlayDTO> getMoviePlays(int movieId) {
        MovieBookingSystemPlayExample playCriteria = new MovieBookingSystemPlayExample();
        playCriteria.createCriteria().andMovieIdEqualTo(movieId);
        List<MovieBookingSystemPlay> movieBookingSystemPlays = playMapper.selectByExample(playCriteria);
        return movieBookingSystemPlays.stream().map(e -> {
            MoviePlayDTO moviePlayDTO = new MoviePlayDTO();
            moviePlayDTO.setPlayId(e.getPlayId());
            moviePlayDTO.setPlayStart(format.format(e.getStartFrom()));
            moviePlayDTO.setPlayEnd(format.format(e.getEndAt()));
            moviePlayDTO.setPrice(e.getPrice());
            TheatreInfo theatreInfo = theatreInfoMapper.selectByPrimaryKey(e.getTheatreId());
            moviePlayDTO.setTheatreAddr(theatreInfo.getTheatreAddress());
            moviePlayDTO.setTheatreLat(theatreInfo.getTheatreAddressLat());
            moviePlayDTO.setTheatreLon(theatreInfo.getTheatreAddressLon());
            moviePlayDTO.setTheatreName(theatreInfo.getTheatreName());
            moviePlayDTO.setSeatsOccupied(e.getSeatsOccupied());
            moviePlayDTO.setSeatsTotal(e.getSeatsTotal());
            moviePlayDTO.setHallNo(e.getHallNo());
            return moviePlayDTO;
        }).collect(Collectors.toList());
    }


    public boolean[] getOccupations(int playId) {
        MovieBookingSystemPlay movieBookingSystemPlay = playMapper.selectByPrimaryKey(playId);
        int seatsTotal = movieBookingSystemPlay.getSeatsTotal();
        boolean[] occupation = new boolean[seatsTotal];
        Arrays.fill(occupation, false);
        MovieBookingSeatsExample criteria = new MovieBookingSeatsExample();
        criteria.createCriteria().andPlayIdEqualTo(playId);
        List<MovieBookingSeats> movieBookingSeats = seatsMapper.selectByExample(criteria);
        for (MovieBookingSeats movieBookingSeat : movieBookingSeats) {
            occupation[movieBookingSeat.getSeatNo()] = true;
        }
        return occupation;
    }
}
