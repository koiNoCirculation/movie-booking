package dev.youtiao.movie_booking.controller;

import dev.youtiao.movie_booking.dto.RegistrationDTO;
import dev.youtiao.movie_booking.dto.Response;
import dev.youtiao.movie_booking.service.RegService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Resource
    private RegService regService;
    private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @PostMapping("/api/register")
    public Response register(@RequestBody RegistrationDTO dto) {
        logger.info("Registering user named {}", dto.getUsername());
        return regService.register(dto);
    }
}
