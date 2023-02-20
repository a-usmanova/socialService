package ru.skillbox.diplom.group32.social.service.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.model.auth.Role;
import ru.skillbox.diplom.group32.social.service.model.auth.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService {

    private User prepareDummyUser(Jwt jwt) {
        User user = new User();
        user.setId(jwt.getClaim("id") != null ? Long.parseLong(jwt.getClaim("id").toString()) : 9999999999999L);
        user.setEmail(jwt.getClaim("email"));
        user.setIsDeleted(false);
        Role role = new Role();
        role.setIsDeleted(false);
        role.setName(jwt.getClaim("roles").toString());
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        return user;
    }

    public UserDetails loadUserFromJwt(Jwt jwt) {
        JwtUser jwtUser = JwtUserFactory.create(prepareDummyUser(jwt));
        log.info("User with email: {} successfully loaded", jwtUser.getEmail());
        jwtUser.getAuthorities().forEach(auth -> log.info("User authorities - " + auth));
        return jwtUser;
    }

    public UserDetails loadSurvey(Jwt jwt) {
        JwtUser jwtUser = JwtUserFactory.create(prepareDummyUser(jwt));
        log.info("Service user: successfully loaded");
        return jwtUser;
    }
}
