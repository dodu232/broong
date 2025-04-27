package org.example.broong.security.auth;

import java.util.Collection;
import lombok.Getter;
import org.example.broong.domain.user.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String userName;
    private final String password;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String email, String password,UserType userType,
            Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userName = email;
        this.password = password;
        this.userType = userType;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
