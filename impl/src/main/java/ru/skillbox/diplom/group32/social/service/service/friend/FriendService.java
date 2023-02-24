package ru.skillbox.diplom.group32.social.service.service.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.model.friend.Friend_;
import ru.skillbox.diplom.group32.social.service.model.notification.EventNotification;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.repository.friend.FriendRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.auth.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
public
class FriendService {

    private FriendRepository friendRepository;
    private AccountService accountService;
    private FriendMapper friendMapper;
    private UserService userService;
    private final KafkaTemplate<String, EventNotification> eventNotificationKafkaTemplate;

    @Autowired
    public FriendService(FriendRepository friendRepository, @Lazy AccountService accountService,
                         FriendMapper friendMapper, UserService userService, KafkaTemplate<String,
            EventNotification> eventNotificationKafkaTemplate) {

        this.friendRepository = friendRepository;
        this.accountService = accountService;
        this.friendMapper = friendMapper;
        this.userService = userService;
        this.eventNotificationKafkaTemplate = eventNotificationKafkaTemplate;

    }

    public FriendDto getById(Long id) {

        log.info("FriendService in getById tried to find friend with id: " + id);
        return friendMapper.convertToDto(friendRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<FriendDto> getAll(FriendSearchDto searchDto, Pageable page) {

        log.info("FriendService in getAll tried to find friends with FriendSearchDto: {} and pageable: {}", searchDto, page);

        if (checkIfAccountSearch(searchDto)) {
            return accountService.searchAccount(friendMapper.friendSearchDtoToAccountSearchDto(searchDto), page)
                    .map(friendMapper::accountDtoToFriendDto);
        }

        if (searchDto.getStatusCode() == null)
            searchDto.setStatusCode(StatusCode.NONE);

        searchDto.setId_from(getJwtUserIdFromSecurityContext());

        List<Friend> friendList = friendRepository.findAll(getSpecification(searchDto));
        List<FriendDto> friendDtos = new ArrayList<>();

        for (Friend friend : friendList) {
            if (searchDto.getStatusCode().equals(StatusCode.BLOCKED) || !friend.getStatusCode().equals(StatusCode.BLOCKED)) {

                FriendDto friendDto = friendMapper.accountDtoToFriendDto(accountService.getAccountById(friend.getToAccountId()));
                friendDto.setStatusCode(friend.getStatusCode());
                friendDtos.add(friendDto);
            }
        }

        Page<FriendDto> pageFriendDto = new PageImpl<>(friendDtos, page, friendDtos.toArray().length);

        return pageFriendDto;

    }

    public void deleteById(Long id) {

        log.info("FriendService in deleteById tried to delete friend with id: " + id);

        if (!checkIfImBlocked(id)) {

            List<Friend> friendList = getCurrentFriendsByAccountId(id);
            List<Long> friendsFriendsList = new ArrayList<>();

            friendList.forEach(f -> {
                f.setStatusCode(StatusCode.NONE);
                friendsFriendsList.add(f.getFromAccountId());
                friendsFriendsList.add(id);
                friendRepository.delete(f);
            });
            friendsFriendsList.forEach(this::createRecommendations);
        }
    }

    private Specification<Friend> getSpecification(FriendSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(Friend_.id, searchDto.getIds(), true))
                .and(equal(Friend_.statusCode, searchDto.getStatusCode(), true)
                        .and(equal(Friend_.fromAccountId, searchDto.getId_from(), true))
                        .and(equal(Friend_.toAccountId, searchDto.getId_to(), true)));
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

        Long userNow = getJwtUserIdFromSecurityContext();
        log.info("FriendService in addFriend has new friend - user with id {} to save: ", id);
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friendMapper.userDtoToFriend(userService.getUser(id)));
        friendList.add(friendMapper.userDtoToFriendFrom(userService.getUser(id)));
        List<Friend> currentFriends = getCurrentFriendsByAccountId(id);
        if (currentFriends.isEmpty()) {
            for (Friend friend: friendList) {
                if (friend.getStatusCode().equals(StatusCode.REQUEST_TO)) {
                    EventNotification eventNotification = new EventNotification(friend.getFromAccountId(), friend.getToAccountId(), NotificationType.FRIEND_REQUEST, "НОВАЯ ЗАЯВКА В ДРУЗЬЯ");
                    eventNotificationKafkaTemplate.send("event-notification", eventNotification);
                }
            }
            return friendMapper.convertToDtoList(friendRepository.saveAll(friendList));
        } else {
            for (Friend friend : currentFriends) {
                if (friend.getStatusCode().equals(StatusCode.BLOCKED)) {
                    if (friend.getFromAccountId().equals(userNow)) {
                        deleteById(id);
                        addFriend(id);
                    }
                } else if (friend.getStatusCode().equals(StatusCode.RECOMMENDATION)) {

                    if (friend.getFromAccountId().equals(userNow)) {
                        friend.setStatusCode(StatusCode.REQUEST_TO);
                        EventNotification eventNotification = new EventNotification(friend.getFromAccountId(), friend.getToAccountId(), NotificationType.FRIEND_REQUEST, "НОВАЯ ЗАЯВКА В ДРУЗЬЯ");
                        eventNotificationKafkaTemplate.send("event-notification", eventNotification);
                    } else
                        friend.setStatusCode(StatusCode.REQUEST_FROM);
                    friendRepository.save(friend);

                }
            }
        }

        return friendMapper.convertToDtoList(currentFriends);
    }

    public void approveFriend(Long id) {

        List<Friend> friendList = getCurrentFriendsByAccountId(id);
        List<Long> friendsFriendsList = new ArrayList<>();
        if (!friendList.isEmpty()) {
            friendList.forEach(e -> {
                if (e.getStatusCode().equals(StatusCode.REQUEST_TO)) {
                    EventNotification eventNotification = new EventNotification(e.getToAccountId(), e.getFromAccountId(), NotificationType.FRIEND_REQUEST, "ВАША ЗАЯВКА В ДРУЗЬЯ ПРИНЯТА");
                    eventNotificationKafkaTemplate.send("event-notification", eventNotification);
                }
                e.setStatusCode(StatusCode.FRIEND);
                friendRepository.save(e);
                friendsFriendsList.addAll(getFriendsIds(e.getFromAccountId()));
            });
            friendsFriendsList.forEach(this::createRecommendations);
        }
    }


    private List<Friend> getCurrentFriendsByAccountId(Long id) {

        List<Friend> list = getForwardFriend(id);
        list.addAll(getBackwardFriend(id));
        return list;

    }

    public void blockFriend(Long id) {

        List<Friend> forwardFriend = getForwardFriend(id);
        List<Friend> backwardFriend = getBackwardFriend(id);

        if (forwardFriend.isEmpty() && backwardFriend.isEmpty()) {
            blockIfNoConnection(id);
        }
        if (forwardFriend.isEmpty()) {
            if (checkIfImBlocked(id)) {
                blockIfBlocked(id);
            }
            ;
        }
        forwardFriend.forEach(f -> {

            switch (f.getStatusCode()) {
                case BLOCKED -> {
                    deleteById(id);
                }
                default -> {
                    if (!checkIfImBlocked(id)) {

                        deleteById(id);
                        blockIfNoConnection(id);

                    } else {

                        blockIfBlocked(id);
                    }
                }
            }
        });
    }

    //
    //==================================================================================================================
    //

    private boolean checkIfAccountSearch(FriendSearchDto searchDto) {

        return (searchDto.getFirstName() != null || searchDto.getAgeFrom() != null ||
                searchDto.getAgeTo() != null || searchDto.getCity() != null || searchDto.getCountry() != null);

    }

    private void blockIfBlocked(Long id) {

        Friend friend = new Friend();
        friend.setStatusCode(StatusCode.BLOCKED);
        friend.setFromAccountId(getJwtUserIdFromSecurityContext());
        friend.setToAccountId(id);
        friend.setIsDeleted(false);
        friendRepository.deleteAll(getForwardFriend(id));
        friendRepository.save(friend);

    }

    private void blockIfNoConnection(Long id) {

        Friend friend = new Friend();
        friend.setStatusCode(StatusCode.BLOCKED);
        friend.setFromAccountId(getJwtUserIdFromSecurityContext());
        friend.setToAccountId(id);
        friend.setIsDeleted(false);
        friendRepository.deleteAll(getForwardFriend(id));
        friendRepository.deleteAll(getBackwardFriend(id));
        friendRepository.save(friend);

    }

    private List<Friend> getForwardFriend(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(getJwtUserIdFromSecurityContext());
        searchDto.setId_to(id);
        return friendRepository.findAll(getSpecification(searchDto));

    }

    public StatusCode getStatus(Long id) {
        List<Friend> friend = getForwardFriend(id);
        if (friend.isEmpty()) {
            return StatusCode.NONE;
        }
        return friend.get(0).getStatusCode();
    }

    private List<Friend> getBackwardFriend(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(id);
        searchDto.setId_to(getJwtUserIdFromSecurityContext());
        return friendRepository.findAll(getSpecification(searchDto));

    }

    private Boolean checkIfImBlocked(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setId_from(id);
        searchDto.setId_to(getJwtUserIdFromSecurityContext());
        searchDto.setStatusCode(StatusCode.BLOCKED);
        return !friendRepository.findAll(getSpecification(searchDto)).isEmpty();

    }

    public List<Long> getBlockedFriendsIds() {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_from(getJwtUserIdFromSecurityContext());
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getToAccountId).collect(Collectors.toList());
    }

    public List<Long> getBlockedFriendsIds(Long id) {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_from(id);
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getToAccountId).collect(Collectors.toList());
    }

    public List<Long> getFriendsIdsWhoBlocked(Long id) {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_to(id);
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getFromAccountId).collect(Collectors.toList());
    }

    public List<Long> getRequestsTo(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_from(id);
        friendSearchDto.setStatusCode(StatusCode.REQUEST_TO);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getToAccountId).collect(Collectors.toList());

    }

    public List<Long> getRequestsFrom(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setId_to(id);
        friendSearchDto.setStatusCode(StatusCode.REQUEST_TO);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getFromAccountId).collect(Collectors.toList());

    }

    //
    //=================================================RECOMMENDATIONS==================================================
    //

    public void createRecommendations(Long id) {

        removeRecommendations(id);
        List<Long> myFriendsId = getFriendsIds(id);
        myFriendsId.add(id);
        myFriendsId.addAll(getBlockedFriendsIds(id));
        myFriendsId.addAll(getFriendsIdsWhoBlocked(id));
        myFriendsId.addAll(getRequestsTo(id));
        myFriendsId.addAll(getRequestsFrom(id));
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

        log.info("RECOMMENDATIONS LIST" + resultList);

    }
    public void removeRecommendations(Long id) {

        List<Long> myFriendsId = getFriendsIds(id);
        myFriendsId.add(id);
        List<Long> allIds = new ArrayList<>(myFriendsId);

        for (Long friendId : myFriendsId) {
            allIds.addAll(getFriendsIds(friendId));
        }

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIds(allIds);
        friendSearchDto.setStatusCode(StatusCode.RECOMMENDATION);
        friendRepository.deleteAll(friendRepository.findAll(getSpecification(friendSearchDto)));

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
    }

    private List<Friend> getRecommendationsFromDB(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.RECOMMENDATION);
        friendSearchDto.setId_from(id);

        return friendRepository.findAll(getSpecification(friendSearchDto));

    }


}
