package dev.youtiao.movie_booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliveController {
    @GetMapping("/alive")
    public String alive() {
        return "";
    }
}
