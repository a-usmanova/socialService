package ru.skillbox.diplom.group32.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.JwtTokenProvider;
import ru.skillbox.diplom.group32.social.service.config.security.exception.PasswordsAreNotMatchingException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.UserAlreadyExistsException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.UserNotFoundException;
import ru.skillbox.diplom.group32.social.service.config.security.exception.WrongPasswordException;
import ru.skillbox.diplom.group32.social.service.model.auth.*;
import ru.skillbox.diplom.group32.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group32.social.service.mapper.auth.UserMapper;

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
        String email = registrationDto.getEmail();

        // TODO - заменить метод на exists()
        userRepository.findUserByEmail(email).ifPresent(x -> { throw new UserAlreadyExistsException("This email already taken");});


        if (!registrationDto.getPassword1().equals(registrationDto.getPassword2())) {
            throw new PasswordsAreNotMatchingException("Passwords should be equal");
        }


        // TODO - mapper
        UserDto userDto = new UserDto();
        userDto.setEmail(registrationDto.getEmail());
        userDto.setFirstname(registrationDto.getFirstName());
        userDto.setLastname(registrationDto.getLastName());
        userDto.setPassword(registrationDto.getPassword1());
        userDto.setIsDeleted(false);

        return createUser(userDto);
    }

    public UserDto createUser(UserDto userDto) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findByName("USER"));

        User userToDB = userMapper.dtoToUser(userDto);
        userToDB.setRoles(userRoles);

        userToDB.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.info("Created User to save - " + userToDB);

        userRepository.save(userToDB);
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
