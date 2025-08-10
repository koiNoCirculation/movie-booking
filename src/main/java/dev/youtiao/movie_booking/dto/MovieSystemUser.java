package dev.youtiao.movie_booking.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MovieSystemUser extends User {
    private String displayName;
    private Integer userId;
    public MovieSystemUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public MovieSystemUser(String username, String password, Integer userId, String displayName, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities);
        this.displayName = displayName;
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
