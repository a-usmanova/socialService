package ru.skillbox.diplom.group32.social.service.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.Account;
import ru.skillbox.diplom.group32.social.service.mapper.AccountMapper;
import ru.skillbox.diplom.group32.social.service.model.User;
import ru.skillbox.diplom.group32.social.service.model.UserDto;
import ru.skillbox.diplom.group32.social.service.model.UserSearchDto;
import ru.skillbox.diplom.group32.social.service.model.User_;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity_;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;
import ru.skillbox.diplom.group32.social.service.repository.UserRepository;

import java.util.List;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.equal;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.in;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final AccountMapper accountMapper;


    public Page<UserDto> getAllUser(UserSearchDto dto) {
        User userFromDB = userRepository.findById(dto.getId()).get();
        return new PageImpl<>(List.of(new UserDto()));
    }

    public UserDto createUser(UserDto userDto) {
        User userToDB = new User();
        accountMapper.AccountToDto(new Account());
        log.info("User to save - " + userToDB);
        userRepository.save(userToDB);
        log.info("User saved to db - " + userToDB);
        log.info("asdasd");
        userDto.setId(userToDB.getId());
        return userDto;
    }

    public UserDto getUser(Long id) {
        User userFromDB = userRepository.findById(id).get();
        getSpecification(new UserSearchDto());
        return new UserDto();
    }

    public void deleteUserById(Long id) {
//        log.info("User ID to del - " + id);
        userRepository.deleteById(id);
    }

    public static Specification<User> getSpecification(UserSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(User_.name, searchDto.getNames(), true));
    }

    private static Specification getBaseSpecification(BaseSearchDto searchDto) {
        return equal(BaseEntity_.id, searchDto.getId(), true)
                .and(equal(BaseEntity_.isDeleted, searchDto.getIsDeleted(), true));
    }
}
