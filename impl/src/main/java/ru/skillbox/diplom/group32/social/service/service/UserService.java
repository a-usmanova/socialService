package ru.skillbox.diplom.group32.social.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.model.User;
import ru.skillbox.diplom.group32.social.service.model.UserDto;
import ru.skillbox.diplom.group32.social.service.repository.UserRepository;

import javax.annotation.Resource;

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

    public UserDto getUser(Long id) {
        User userFromDB = userRepository.findById(id).get();
        return new UserDto(userFromDB.getId(), userFromDB.getName(), userFromDB.getAge());
    }

    public void deleteUser(UserDto userDto) {
        User userForDel = new User(userDto.getId(), userDto.getName(), userDto.getAge());
        log.info("User to del - " + userForDel);
        userRepository.delete(userForDel);
    }

    public void deleteUserById(Long id) {
        log.info("User ID to del - " + id);
        userRepository.deleteById(id);
    }

    public void hardDeleteUser(UserDto userDto) {
        User userForDel = new User(userDto.getId(), userDto.getName(), userDto.getAge());
        log.info("User to hard del - " + userForDel);
        userRepository.hardDelete(userForDel);
    }

    public void hardDeleteUserById(Long id) {
        log.info("User ID to hard del - " + id);
        userRepository.hardDeleteById(id);
    }


}
