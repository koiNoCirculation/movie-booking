package dev.youtiao.movie_booking.controller;

import dev.youtiao.movie_booking.dto.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String login() {
        return "index";
    }

    @RequestMapping("/status")
    public Response status() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Response.ResponseBuilder resp = new Response.ResponseBuilder();
        if(authentication != null) {
            resp.setSucceed(true);
        } else {
            resp.setSucceed(false);
        }
        return resp.build();
    }

    @RequestMapping("/logout")
    public String logout() {
        return "index";
    }
}
