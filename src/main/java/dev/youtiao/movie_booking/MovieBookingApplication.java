package dev.youtiao.movie_booking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("dev.youtiao.movie_booking.dao")
public class MovieBookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MovieBookingApplication.class, args);
	}

}
