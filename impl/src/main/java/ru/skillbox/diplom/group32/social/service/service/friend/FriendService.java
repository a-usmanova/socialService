package ru.skillbox.diplom.group32.social.service.service.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend_;
import ru.skillbox.diplom.group32.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.auth.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final AccountService accountService;
    private final FriendMapper friendMapper;

    private final UserService userService;

    //выдает блокированных в поиске
    //проходит двойная отправка заявки в друзья

    public FriendDto getById(Long id) {

        log.info("FriendService in getById tried to find friend with id: " + id);
        return friendMapper.convertToDto(friendRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<FriendDto> getAll(FriendSearchDto searchDto, Pageable page) {

        log.info("FriendService in getAll tried to find friends with FriendSearchDto: {} and pageable: {}", searchDto, page);

        if (searchDto.getStatusCode() == null) {
            searchDto.setStatusCode(StatusCode.NONE);
        } else {
            searchDto.setId_from(getJwtUserIdFromSecurityContext());
        }

        Page<Friend> friendPage = friendRepository.findAll(getSpecification(searchDto), page);
        Page<FriendDto> friendDtos = friendPage.map(friendMapper::convertToDto);
        friendDtos.map(e -> {
            e.setFirstName(accountService.getAccountById(e.getToAccountId()).getFirstName());
            e.setLastName(accountService.getAccountById(e.getToAccountId()).getLastName());
            e.setStatusCode(friendRepository.findById(e.getId()).get().getStatusCode());
            return e;
        });

        return friendDtos;

    }

    public void deleteById(Long id) {

        log.info("FriendService in deleteById tried to delete friend with id: " + id);
        List<Friend> friendList = getCurrentFriendsByAccountId(id);
        List<Long> friendsFriendsList = new ArrayList<>();
        friendList.forEach(f -> {
            friendRepository.delete(f);
            friendsFriendsList.add(f.getFromAccountId());
            friendsFriendsList.addAll(getFriendsIds(f.getFromAccountId()));
        });
        friendsFriendsList.forEach(this::createRecommendations);
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

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.FRIEND);
        friendSearchDto.setId_from(getJwtUserIdFromSecurityContext());
        return friendRepository.findAll(getSpecification(friendSearchDto)).stream().map(Friend::getToAccountId).collect(Collectors.toList());

    }

    public List<Long> getFriendsIds(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.FRIEND);
        friendSearchDto.setId_from(id);
        return friendRepository.findAll(getSpecification(friendSearchDto)).stream().map(Friend::getToAccountId).collect(Collectors.toList());

    }

    public Integer getCount() {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.REQUEST_FROM);
        friendSearchDto.setId_from(getJwtUserIdFromSecurityContext());

        return friendRepository.findAll(getSpecification(friendSearchDto)).size();

    }

    public List<FriendDto> addFriend(Long id) {

        log.info("FriendService in addFriend has new friend - user with id {} to save: ", id);
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friendMapper.userDtoToFriend(userService.getUser(id)));
        friendList.add(friendMapper.userDtoToFriendFrom(userService.getUser(id)));
        List<Friend> currentFriends = getCurrentFriendsByAccountId(id);
        if (currentFriends.isEmpty()) {
            return friendMapper.convertToDtoList(friendRepository.saveAll(friendList));
        } else {
            for (Friend friend : currentFriends) {
                if (friend.getStatusCode().equals(StatusCode.FRIEND)) {
                    continue;
                }
                friend.setStatusCode(Objects.equals(friend.getFromAccountId(), getJwtUserIdFromSecurityContext())
                        & friend.getStatusCode() == (StatusCode.RECOMMENDATION) ?
                        StatusCode.REQUEST_TO : StatusCode.REQUEST_FROM);
                friendRepository.save(friend);
            }

        }
            return friendMapper.convertToDtoList(currentFriends);
    }

    public void approveFriend(Long id) {
      // в идеале переделать на account id
        List<Friend> friendList = getCurrentFriendsByFriendId(id);
        List<Long> friendsFriendsList = new ArrayList<>();
        if (!friendList.isEmpty()) {
            friendList.forEach(e -> {
                e.setStatusCode(StatusCode.FRIEND);
                friendRepository.save(e);
                friendsFriendsList.addAll(getFriendsIds(e.getFromAccountId()));
            });
            friendsFriendsList.forEach(this::createRecommendations);
        }
    }

    private List<Friend> getCurrentFriendsByFriendId(Long id) {

        Friend initialFriend = friendRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_to(initialFriend.getFromAccountId());
        friendSearchDto.setId_from(initialFriend.getToAccountId());
        List<Friend> list = friendRepository.findAll(getSpecification(friendSearchDto));
        friendSearchDto.setId_to(initialFriend.getToAccountId());
        friendSearchDto.setId_from(initialFriend.getFromAccountId());
        list.addAll(friendRepository.findAll(getSpecification(friendSearchDto)));

        return list;

    }

    private List<Friend> getCurrentFriendsByAccountId(Long id) {

        List<Friend> list = getForwardFriend(id);
        list.addAll(getBackwardFriend(id));
        return list;

    }

    public void blockFriend(Long id) {

        List<Friend> friendList = getForwardFriend(id);

        if (friendList.isEmpty()) {
            blockIfNoConnection(id);
        }
        friendList.forEach(f -> {
            switch (f.getStatusCode()) {
                case BLOCKED -> {
                    friendRepository.delete(f);
                }
                default -> {

                    friendRepository.deleteAll(getCurrentFriendsByAccountId(id));
                    blockIfNoConnection(id);

                }
            }
        });
        friendRepository.saveAll(friendList);
    }

    private void blockIfNoConnection(Long id) {

        Friend friend = new Friend();
        friend.setStatusCode(StatusCode.BLOCKED);
        friend.setFromAccountId(getJwtUserIdFromSecurityContext());
        friend.setToAccountId(id);
        friend.setIsDeleted(false);
        friendRepository.save(friend);

    }

    private List<Friend> getForwardFriend(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(getJwtUserIdFromSecurityContext());
        searchDto.setId_to(id);
        return friendRepository.findAll(getSpecification(searchDto));

    }

    private List<Friend> getBackwardFriend(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(id);
        searchDto.setId_to(getJwtUserIdFromSecurityContext());
        return friendRepository.findAll(getSpecification(searchDto));

    }

    public List<Long> getBlockedFriendsIds() {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_from(getJwtUserIdFromSecurityContext());
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    public void sendNotice() {
        // ??
    }

    public void createRecommendations(Long id) {

        List<Long> myFriendsId = getFriendsIds(id);
        myFriendsId.add(id);
        Map<Long, Long> friendsFriendsId = new TreeMap<>();
        for (Long friendId : myFriendsId) {
            List<Long> list = getFriendsIds(friendId);
            for (Long f : list) {
                if (myFriendsId.contains(f)) continue;
                if (friendsFriendsId.containsKey(f)) {
                    friendsFriendsId.put(f, friendsFriendsId.get(f) + 1L);
                } else {
                    friendsFriendsId.put(f, 1L);
                }
            }
        }

        log.info("RECOMMENDATIONS MAP" + friendsFriendsId);

        friendRepository.deleteAll(getRecommendationsFromDB(id));

        List<Long> resultList = friendsFriendsId.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        Long count = 0L;

        for (Long entry : resultList) {
            count++;
            Friend friend = new Friend();
            friend.setStatusCode(StatusCode.RECOMMENDATION);
            friend.setFromAccountId(id);
            friend.setToAccountId(entry);
            friend.setRating(count);
            friend.setIsDeleted(false);
            friendRepository.save(friend);
        }

//                .collect(Collectors.toMap(
//                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        log.info("RECOMMENDATIONS LIST" + resultList);

    }

    public List<FriendDto> getRecommendation() {

        List<FriendDto> friendDtos = new ArrayList<>();
        for (Friend friend : getRecommendationsFromDB(getJwtUserIdFromSecurityContext())) {
            FriendDto friendDto = friendMapper.userDtoToFriendDto(accountService.getAccountById(friend.getToAccountId()));
            friendDto.setRating(friend.getRating());
            friendDtos.add(friendDto);
        }
        friendDtos.sort(Comparator.comparing(FriendDto::getRating));

        return friendDtos;

        //**TODO сделать пэйдж и таблицу recommendation
    }

    private List<Friend> getRecommendationsFromDB(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.RECOMMENDATION);
        friendSearchDto.setId_from(id);

        return friendRepository.findAll(getSpecification(friendSearchDto));

    }

}
