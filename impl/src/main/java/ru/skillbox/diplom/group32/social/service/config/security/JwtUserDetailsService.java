package ru.skillbox.diplom.group32.social.service.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.exception.JwtAuthenticationException;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group32.social.service.service.auth.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).get();

        if (user == null) {
            throw new JwtAuthenticationException("User with email " + email + " not found.");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("User with email: {} successfully loaded", email);
        jwtUser.getAuthorities().forEach(auth ->  log.info("User authorities - " + auth));
        return jwtUser;
    }
}
