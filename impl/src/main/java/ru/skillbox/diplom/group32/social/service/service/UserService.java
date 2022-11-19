package ru.skillbox.diplom.group32.social.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.model.User;
import ru.skillbox.diplom.group32.social.service.model.UserDto;
import ru.skillbox.diplom.group32.social.service.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;


    public UserDto createUser(UserDto userDto) {
        User userToDB = new User(userDto.getName(), userDto.getAge());
        log.info("User to save - " + userToDB);
        userRepository.save(userToDB);
        log.info("User saved to db - " + userToDB);
        userDto.setId(userToDB.getId());
        return userDto;
    }

    public UserDto getUser(Integer id) {
        User userFromDB = userRepository.findById(id).get();
        return new UserDto(userFromDB.getId(), userFromDB.getName(), userFromDB.getAge());
    }

}
