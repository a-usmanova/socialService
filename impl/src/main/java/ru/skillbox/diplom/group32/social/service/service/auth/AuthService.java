package ru.skillbox.diplom.group32.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.JwtTokenProvider;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateDto;
import ru.skillbox.diplom.group32.social.service.model.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        try {
            String email = authenticateDto.getEmail();
            User user = userService.findUserByEmail(email);
            log.info("User with email: " + email + " found");
            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + email + " not found");
            }

            String token = jwtTokenProvider.createToken(user.getId(), email, user.getRoles());


            return new AuthenticateResponseDto(email, token);

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
