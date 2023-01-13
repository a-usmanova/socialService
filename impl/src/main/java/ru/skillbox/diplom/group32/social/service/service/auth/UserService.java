package ru.skillbox.diplom.group32.social.service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.config.security.exception.UserNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.auth.UserMapper;
import ru.skillbox.diplom.group32.social.service.model.auth.Role;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.auth.UserDto;
import ru.skillbox.diplom.group32.social.service.repository.auth.RoleRepository;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Autowired
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public UserDto createUser(UserDto userDto) {
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleRepository.findByName("USER"));

        User userToDB = userMapper.dtoToUser(userDto);
        userToDB.setRoles(userRoles);

        userToDB.setPassword(getPasswordEncoder().encode(userDto.getPassword()));
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
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("User by email is - " + user);
        return user;
    }
}
