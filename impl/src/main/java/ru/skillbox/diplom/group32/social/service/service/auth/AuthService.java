package ru.skillbox.diplom.group32.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.JwtTokenProvider;
import ru.skillbox.diplom.group32.social.service.config.security.exception.*;
import ru.skillbox.diplom.group32.social.service.mapper.auth.UserMapper;
import ru.skillbox.diplom.group32.social.service.model.auth.*;
import ru.skillbox.diplom.group32.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.auth.captcha.CaptchaService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    private final AccountService accountService;

    private final CaptchaService captchaService;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        String email = authenticateDto.getEmail();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        log.info("User with email: " + email + " found");


        if (!passwordEncoder.matches(authenticateDto.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        String token = jwtTokenProvider.createToken(user.getId(), email, user.getRoles());


        return new AuthenticateResponseDto(token, token);
    }


    public UserDto register(RegistrationDto registrationDto) {

        if (!captchaService.passCaptcha(registrationDto)) {
            throw new WrongCaptchaException("Captcha code isn't right");
        }


        String email = registrationDto.getEmail();
        userRepository.findUserByEmail(email).ifPresent(x -> { throw new UserAlreadyExistsException("This email already taken");});

        if (!registrationDto.getPassword1().equals(registrationDto.getPassword2())) {
            throw new PasswordsAreNotMatchingException("Passwords should be equal");
        }

        return createUser(userMapper.registrationDtoToUserDto(registrationDto));
    }

    public UserDto createUser(UserDto userDto) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findByName("USER"));

        User userToDB = userMapper.dtoToUser(userDto);
        userToDB.setRoles(userRoles);

        userToDB.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("Created User to save - " + userToDB);

        accountService.createAccount(userToDB);
        log.info("Created User saved to db - " + userToDB);

        UserDto userDtoResult = userMapper.userToDto(userToDB);
        log.info("Created User Dto result - " + userDtoResult);
        return userDtoResult;
    }

    public UserDto getUser(Long id) {

        User userFromDB = userRepository.findById(id).get();
        UserDto userDtoResult = userMapper.userToDto(userFromDB);
        log.info("Search User Dto result - " + userDtoResult);

        return userDtoResult;
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).get();
        log.info("User by email is - " + user);
        return user;
    }
}
