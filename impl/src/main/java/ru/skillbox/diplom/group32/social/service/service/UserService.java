package ru.skillbox.diplom.group32.social.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.Account;
import ru.skillbox.diplom.group32.social.service.mapper.AccountMapper;
import ru.skillbox.diplom.group32.social.service.model.User;
import ru.skillbox.diplom.group32.social.service.model.UserDto;
import ru.skillbox.diplom.group32.social.service.repository.UserRepository;
import ru.skillbox.diplom.group32.social.service.utils.specification.metamodel.User_;

import java.util.ArrayList;
import java.util.List;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.equal;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.in;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final AccountMapper accountMapper;


    public UserDto createUser(UserDto userDto) {
        User userToDB = new User(userDto.getName(), userDto.getAge());
        accountMapper.AccountToDto(new Account());
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

//    public Page<UserDto> getAllUser(Sea) {
//        User userFromDB = userRepository.findById(id).get();
//        return new UserDto(userFromDB.getId(), userFromDB.getName(), userFromDB.getAge());
//    }

    public void deleteUserById(Long id) {
        log.info("User ID to del - " + id);
        userRepository.deleteById(id);
    }

    public static Specification<User> getSpecification(UserDto userDto) {
        List<String> nameList = new ArrayList<>();
        nameList.add(userDto.getName());
        return in(User_.name, nameList, true)
                .and(equal(User_.id, userDto.getId(), true));
    }
}
