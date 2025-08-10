package dev.youtiao.movie_booking;

import dev.youtiao.movie_booking.dao.MovieBookingSeatsMapper;
import dev.youtiao.movie_booking.dao.MovieBookingSystemPlayMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSeats;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemPlay;
import dev.youtiao.movie_booking.service.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieBookingSystemPlayMapper playMapper;

    @Mock
    private MovieBookingSeatsMapper seatsMapper;

    @InjectMocks
    private MovieService movieService;

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
}
