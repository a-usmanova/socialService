package ru.skillbox.diplom.group32.social.service.service.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group32.social.service.mapper.auth.UserMapper;
import ru.skillbox.diplom.group32.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend_;
import ru.skillbox.diplom.group32.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group32.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.auth.UserService;

import java.util.List;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final FriendMapper friendMapper;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    private final UserService userService;

    public FriendDto getById(Long id) {

        log.info("FriendService in getById tried to find friend with id: " + id);
        return friendMapper.convertToDto(friendRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<FriendDto> getAll(FriendSearchDto searchDto, Pageable page) {

        log.info("FriendService in getAll tried to find friends with FriendSearchDto: {} and pageable: {}", searchDto, page);

        searchDto.setIsDeleted(false);
        if (searchDto.getStatusCode() == null) {
            searchDto.setStatusCode(StatusCode.NONE);
        }
        else if (searchDto.getStatusCode().equals(StatusCode.REQUEST_FROM)) {
            searchDto.setId_to(getJwtUserIdFromSecurityContext());
        } else if (searchDto.getStatusCode().equals(StatusCode.REQUEST_TO)) {
            searchDto.setId_to(getJwtUserIdFromSecurityContext());
        }

        Page<Friend> friendPage = friendRepository.findAll(getSpecification(searchDto), page);
        Page<FriendDto> friendDtos = friendPage.map(friendMapper::convertToDto);
        friendDtos.map(e -> {
            if (e.getStatusCode().equals(StatusCode.REQUEST_FROM)) {
                e.setFirstName(accountService.getAccountById(e.getFromAccountId()).getFirstName());
                e.setLastName(accountService.getAccountById(e.getFromAccountId()).getLastName());
            }if (e.getStatusCode().equals(StatusCode.REQUEST_TO)) {
                e.setFirstName(accountService.getAccountById(e.getFromAccountId()).getFirstName());
                e.setLastName(accountService.getAccountById(e.getFromAccountId()).getLastName());
            }
            return e;

        });

        return friendDtos;

    }

    public void deleteById(Long id) {

        log.info("FriendService in deleteById tried to delete friend with id: " + id);

        Friend friend = friendRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(friend.getToAccountId());
        searchDto.setId_to(friend.getFromAccountId());
        Page<Friend> friendPage = friendRepository.findAll(getSpecification(searchDto), Pageable.unpaged());
        friendRepository.deleteAll(friendPage);
        friendRepository.deleteById(id);

    }

    private Specification<Friend> getSpecification(FriendSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(Friend_.id, searchDto.getIds(), true))
                .and(equal(Friend_.statusCode, searchDto.getStatusCode(), true)
                .and(equal(Friend_.fromAccountId, searchDto.getId_from(), true))
                .and(equal(Friend_.toAccountId, searchDto.getId_to(), true)));
//                .and(like(Friend_.firstName, searchDto.getFirstName(), true))
//                .and(between(Friend_.birthDate, searchDto.getBirthDateFrom(), searchDto.getBirthDateFrom(), true))
//                .and(like(Friend_.city, searchDto.getCity(), true))
//                .and(like(Friend_.country, searchDto.getCountry(), true));
    }

    public List<Long> getFriendsIds() {

        return null;

    }

    public Integer getCount() {

        final Integer count = 0;

        return null;


    }

    public FriendDto addFriend(Long id) {

// *TODO на каждую дружбу две записи

        log.info("FriendService in addFriend has new friend - user with {} to save: ", id);
        Friend friendTo = friendMapper.userDtoToFriend(userService.getUser(id));
        Friend friendFrom = friendMapper.userDtoToFriendFrom(userService.getUser(id));
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_from(friendFrom.getFromAccountId());
        friendSearchDto.setIsDeleted(false);
        friendSearchDto.setId_to(id);
        List<Friend> friendSearch = friendRepository.findAll(getSpecification(friendSearchDto));
        if (friendSearch.isEmpty()) {
            friendRepository.save(friendFrom);
            return friendMapper.convertToDto(friendRepository.save(friendTo));
        } else return friendMapper.convertToDto(friendSearch.stream().findFirst().orElseThrow(ObjectNotFoundException::new));
    }
}
