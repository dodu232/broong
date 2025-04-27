package org.example.broong.security.auth;

import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User findUser = userRepository.findByEmail(username).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "존재하지 않는 유저 입니다."));


        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+findUser.getUserType().name()));

        return new CustomUserDetails(findUser.getId(), findUser.getEmail(), findUser.getPassword(), findUser.getUserType(), findUser.getDeletedAt(), authorities);

    }
}
