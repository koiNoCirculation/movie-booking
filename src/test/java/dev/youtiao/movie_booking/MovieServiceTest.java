package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemMoviesMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.RatingMovieMapper;
import dev.youtiao.movie_booking.dao.TheatreInfoMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemMovies;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.dao.entities.RatingMovie;
import dev.youtiao.movie_booking.dao.entities.TheatreInfo;
import dev.youtiao.movie_booking.dto.MovieDTO;
import dev.youtiao.movie_booking.dto.MoviePlayDTO;
import dev.youtiao.movie_booking.dto.RatingDTO;
import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.service.MovieService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieBookingSystemPlayMapper playMapper;

    @Mock
    private MovieBookingSeatsMapper seatsMapper;

    @Mock
    private MovieBookingSystemMoviesMapper moviesMapper;

    @InjectMocks
    private MovieService movieService;

    @Mock
    private RatingMovieMapper ratingMovieMapper;

    @Mock
    private TheatreInfoMapper theatreInfoMapper;

    @Test
    public void testGetOccupations() {
        MovieBookingSystemPlay play = new MovieBookingSystemPlay();
        play.setSeatsTotal(100);
        when(playMapper.selectByPrimaryKey(any())).thenReturn(play);
        MovieBookingSeats movieBookingSeats = new MovieBookingSeats();
        MovieBookingSeats movieBookingSeats1 = new MovieBookingSeats();
        MovieBookingSeats movieBookingSeats2 = new MovieBookingSeats();
        movieBookingSeats.setSeatNo(1);
        movieBookingSeats1.setSeatNo(50);
        movieBookingSeats2.setSeatNo(99);
        when(seatsMapper.selectByExample(any())).thenReturn(Arrays.asList(movieBookingSeats1,movieBookingSeats2, movieBookingSeats));
        boolean[] occupations = movieService.getOccupations(1);
        Assertions.assertEquals(100, occupations.length);
        Assertions.assertTrue( occupations[1]);
        Assertions.assertTrue(occupations[50]);
        Assertions.assertTrue(occupations[99]);
        for(int i = 0; i < 100; i++) {
            if(i != 1 && i != 50 && i != 99) {
                Assertions.assertFalse(occupations[i]);
            }
        }
    }

    @Test
    public void testGetMovies() {
        MovieBookingSystemMovies movie1 = new MovieBookingSystemMovies();
        movie1.setName("Mov1");
        movie1.setCoverImgUrl("url1");
        movie1.setMovieId(1);
        movie1.setLength(100);
        movie1.setGenre("genre1");
        movie1.setDirector("director1");
        movie1.setIntroduction("introduction1");
        movie1.setCast("cast1");
        MovieBookingSystemMovies movie2 = new MovieBookingSystemMovies();
        movie2.setName("Mov2");
        movie2.setCoverImgUrl("url2");
        movie2.setMovieId(2);
        movie2.setLength(200);
        movie2.setGenre("genre2");
        movie2.setDirector("director2");
        movie2.setIntroduction("introduction2");
        movie2.setCast("cast2");
        when(moviesMapper.selectByExampleWithBLOBs(any())).thenReturn(Arrays.asList(movie1, movie2));
        Response res = movieService.getMovies(1, 5);
        MovieDTO dto1 = new MovieDTO();
        dto1.setName("Mov1");
        dto1.setCoverImageURL("url1");
        dto1.setMovieId(1);
        dto1.setLength(100);
        dto1.setGenre("genre1");
        dto1.setDirector("director1");
        dto1.setIntroduction("introduction1");
        dto1.setCast("cast1");
        MovieDTO dto2 = new MovieDTO();
        dto2.setName("Mov2");
        dto2.setCoverImageURL("url2");
        dto2.setMovieId(2);
        dto2.setLength(200);
        dto2.setGenre("genre2");
        dto2.setDirector("director2");
        dto2.setIntroduction("introduction2");
        dto2.setCast("cast2");
        Assertions.assertEquals(Arrays.asList(dto1, dto2), res.getData());

    }

    @Test
    public void testGetRatings() {
        RatingMovie rating = new RatingMovie();
        rating.setMovieId(1);
        rating.setImdbRating(new BigDecimal(4.5));
        rating.setAuditRatingCount(10);
        rating.setTotalAuditRating(45L);
        rating.setFinalAuditRating(new BigDecimal(4.5));
        when(ratingMovieMapper.selectByPrimaryKey(1)).thenReturn(rating);
        RatingDTO dto = new RatingDTO();
        dto.setAuditorRating("4.5");
        dto.setImdbRating("4.5");
        RatingDTO ratings = movieService.getRatings(1);
        Assertions.assertEquals(dto.getImdbRating(), ratings.getImdbRating());
        Assertions.assertEquals(dto.getAuditorRating(), ratings.getAuditorRating());
    }

    @Test
    public void testGetMoviePlays() {
        MovieBookingSystemPlay play1 = new MovieBookingSystemPlay();
        play1.setPlayId(1);
        play1.setMovieId(1);
        play1.setHallNo(1);
        play1.setStartFrom(new DateTime(2025,1,1,1,1,1).toDate());
        play1.setEndAt(new DateTime(2025,1,1,3,1,1).toDate());
        play1.setPrice(new BigDecimal(25));
        play1.setTheatreId(1);
        play1.setSeatsTotal(100);
        play1.setSeatsOccupied(10);
        MovieBookingSystemPlay play2 = new MovieBookingSystemPlay();
        play2.setPlayId(2);
        play2.setMovieId(1);
        play2.setHallNo(2);
        play2.setStartFrom(new DateTime(2025,1,1,1,1,1).toDate());
        play2.setEndAt(new DateTime(2025,1,1,3,1,1).toDate());
        play2.setPrice(new BigDecimal(25));
        play2.setTheatreId(1);
        play2.setSeatsTotal(100);
        play2.setSeatsOccupied(10);
        when(playMapper.selectByExample(any())).thenReturn(Arrays.asList(play1, play2));
        TheatreInfo theatreInfo = new TheatreInfo();
        theatreInfo.setTheatreAddress("a");
        theatreInfo.setTheatreName("b");
        theatreInfo.setTheatreAddressLat(new BigDecimal(1.0));
        theatreInfo.setTheatreAddressLon(new BigDecimal(2.0));
        when(theatreInfoMapper.selectByPrimaryKey(any())).thenReturn(theatreInfo);
        List<MoviePlayDTO> moviePlays = movieService.getMoviePlays(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MoviePlayDTO dto1 = new MoviePlayDTO();
        dto1.setPlayId(1);
        dto1.setPlayStart(format.format(play1.getStartFrom()));
        dto1.setPlayEnd(format.format(play1.getEndAt()));
        dto1.setPrice(play1.getPrice());
        dto1.setHallNo(play1.getHallNo());
        dto1.setSeatsTotal(play1.getSeatsTotal());
        dto1.setSeatsOccupied(play1.getSeatsOccupied());
        dto1.setTheatreAddr(theatreInfo.getTheatreAddress());
        dto1.setTheatreName(theatreInfo.getTheatreName());
        dto1.setTheatreLon(theatreInfo.getTheatreAddressLon());
        dto1.setTheatreLat(theatreInfo.getTheatreAddressLat());
        MoviePlayDTO dto2 = new MoviePlayDTO();
        dto2.setPlayId(2);
        dto2.setPlayStart(format.format(play2.getStartFrom()));
        dto2.setPlayEnd(format.format(play2.getEndAt()));
        dto2.setPrice(play2.getPrice());
        dto2.setHallNo(play2.getHallNo());
        dto2.setSeatsTotal(play2.getSeatsTotal());
        dto2.setSeatsOccupied(play2.getSeatsOccupied());
        dto2.setTheatreAddr(theatreInfo.getTheatreAddress());
        dto2.setTheatreName(theatreInfo.getTheatreName());
        dto2.setTheatreLon(theatreInfo.getTheatreAddressLon());
        dto2.setTheatreLat(theatreInfo.getTheatreAddressLat());
        Assertions.assertEquals(Arrays.asList(dto1, dto2), moviePlays);
    }
}
