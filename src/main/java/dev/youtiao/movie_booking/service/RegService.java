package dev.youtiao.movie_booking.service;

import dev.youtiao.movie_booking.dao.MovieBookingSystemUsersMapper;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsers;
import dev.youtiao.movie_booking.dao.entities.MovieBookingSystemUsersExample;
import dev.youtiao.movie_booking.dto.RegistrationDTO;
import dev.youtiao.movie_booking.dto.Response;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegService {

    @Resource
    private MovieBookingSystemUsersMapper mapper;

    @Transactional
    public Response register(RegistrationDTO dto) {
        Response.ResponseBuilder builder = new Response.ResponseBuilder();
        String username = dto.getUsername();
        MovieBookingSystemUsersExample exists = new MovieBookingSystemUsersExample();
        exists.createCriteria().andNameEqualTo(username);
        long c = mapper.countByExample(exists);
        if(c > 0) {
            builder.setSucceed(false);
            builder.setMessage("The name of user already exists.");
        } else {
            if(StringUtils.isEmpty(dto.getPassword())) {
                builder.setSucceed(false);
                builder.setMessage("The password cannot be empty");
            } else if(!Strings.CS.equalsAny(dto.getPassword(), dto.getRepeatedpassword())) {
                builder.setSucceed(false);
                builder.setMessage("The password and repeated password is different");

            } else {
                builder.setSucceed(true);
                builder.setMessage("Registration succeeded");
                MovieBookingSystemUsers movieBookingSystemUsers = new MovieBookingSystemUsers();
                movieBookingSystemUsers.setName(dto.getUsername());
                movieBookingSystemUsers.setDisplayName(dto.getUsername());
                movieBookingSystemUsers.setRole("USER");
                movieBookingSystemUsers.setHashedPasswd(dto.getPassword());
                mapper.insertSelective(movieBookingSystemUsers);
            }
        }
        return builder.build();
    }
}
