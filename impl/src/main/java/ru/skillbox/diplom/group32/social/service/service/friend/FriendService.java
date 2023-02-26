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
    private final KafkaTemplate<String, EventNotification> eventNotificationKafkaTemplate;

    @Autowired
    public FriendService(FriendRepository friendRepository, @Lazy AccountService accountService,
                         FriendMapper friendMapper, KafkaTemplate<String,
            EventNotification> eventNotificationKafkaTemplate) {

        this.friendRepository = friendRepository;
        this.accountService = accountService;
        this.friendMapper = friendMapper;
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

        if (searchDto.getStatusCode() == null) {
            searchDto.setStatusCode(StatusCode.NONE);
        }

        searchDto.setIdFrom(getJwtUserIdFromSecurityContext());

        List<Friend> friendList = friendRepository.findAll(getSpecification(searchDto));
        List<FriendDto> friendDtos = new ArrayList<>();

        for (Friend friend : friendList) {
            if (searchDto.getStatusCode().equals(StatusCode.BLOCKED) || !friend.getStatusCode().equals(StatusCode.BLOCKED)) {
                FriendDto friendDto = friendMapper.accountDtoToFriendDto(accountService.getAccountById(friend.getToAccountId()));
                friendDto.setStatusCode(friend.getStatusCode());
                friendDtos.add(friendDto);
            }
        }

        return new PageImpl<>(friendDtos, page, friendDtos.toArray().length);

    }

    public void deleteById(Long id) {

        log.info("FriendService in deleteById tried to delete user with id: " + id);

        if (!checkIfImBlocked(id)) {

            List<Friend> friendList = getCurrentInvoicesByAccountId(id);

            friendList.forEach(f -> {
                f.setStatusCode(StatusCode.NONE);
                friendRepository.delete(f);
            });

            removeRecommendations();
            createRecommendations();

        } else {
            friendRepository.deleteAll(getOutgoingInvoice(id));
        }
    }

    public List<FriendDto> addFriend(Long id) {

        log.info("FriendService in addFriend has new friend - user with id {} to save: ", id);

        List<Friend> currentFriends = getCurrentInvoicesByAccountId(id);
        if (currentFriends.isEmpty()) {
            return createInvoices(id);
        } else {
            for (Friend friend : currentFriends) {
                if (friend.getStatusCode().equals(StatusCode.RECOMMENDATION)) {
                    friendRepository.delete(friend);
                    createInvoices(id);
                }
            }
        }
        return friendMapper.convertToDtoList(currentFriends);
    }

    public void approveFriend(Long id) {

        List<Friend> friendList = getCurrentInvoicesByAccountId(id);

        if (!friendList.isEmpty()) {
            friendList.forEach(friend -> {
                if (friend.getStatusCode().equals(StatusCode.REQUEST_FROM)) {
                    sendNotification(friend, "ЗАЯВКА В ДРУЗЬЯ ПРИНЯТА");
                }
                friend.setStatusCode(StatusCode.FRIEND);
                friendRepository.save(friend);
            });

            removeRecommendations();
            createRecommendations();
        }
    }

    public void blockFriend(Long id) {

        List<Friend> forwardFriend = getOutgoingInvoice(id);
        List<Friend> backwardFriend = getIncomingInvoice(id);

        if (checkIfImBlocked(id)) {
            blockIfBlocked(id);

        } else if (forwardFriend.isEmpty() && backwardFriend.isEmpty()) {
            blockIfNoConnection(id);
        } else {

            forwardFriend.forEach(f -> {
                if (f.getStatusCode() != StatusCode.BLOCKED) {
                    friendRepository.deleteAll(forwardFriend);
                    friendRepository.deleteAll(backwardFriend);
                    blockIfNoConnection(id);
                } else {
                    friendRepository.deleteAll(forwardFriend);
                    friendRepository.deleteAll(backwardFriend);
                }
            });
        }
    }

    public Long getCount() {

        log.info("FriendService in getCount count friends requests for friend with id: " + getJwtUserIdFromSecurityContext());
        return (long) friendRepository.findAll(getSpecification(new FriendSearchDto(getJwtUserIdFromSecurityContext(), StatusCode.REQUEST_FROM))).size();

    }

    //
    //==================================================================================================================
    //

    private boolean checkIfAccountSearch(FriendSearchDto searchDto) {

        return (searchDto.getFirstName() != null || searchDto.getAgeFrom() != null ||
                searchDto.getAgeTo() != null || searchDto.getCity() != null || searchDto.getCountry() != null);

    }

    private List<FriendDto> createInvoices(Long id) {

        if (alreadySendRequests(id).isEmpty()) {
            List<Friend> listFriend = new ArrayList<>();
            listFriend.add(new Friend(getJwtUserIdFromSecurityContext(), StatusCode.REQUEST_TO, id));
            listFriend.add(new Friend(id, StatusCode.REQUEST_FROM, getJwtUserIdFromSecurityContext()));
            friendRepository.saveAll(listFriend);
            sendNotification(listFriend.get(0), "НОВАЯ ЗАЯВКА В ДРУЗЬЯ");
            return friendMapper.convertToDtoList(listFriend);
        }
        return friendMapper.convertToDtoList(alreadySendRequests(id));
    }

    private List<Friend> alreadySendRequests(Long id) {

        List<Friend> listFriend = friendRepository.findAll(getSpecification(new FriendSearchDto(getJwtUserIdFromSecurityContext(), StatusCode.REQUEST_TO, id)));
        listFriend.addAll(friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.REQUEST_TO, getJwtUserIdFromSecurityContext()))));
        listFriend.addAll(friendRepository.findAll(getSpecification(new FriendSearchDto(getJwtUserIdFromSecurityContext(), StatusCode.REQUEST_FROM, id))));
        listFriend.addAll(friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.REQUEST_FROM, getJwtUserIdFromSecurityContext()))));
        return listFriend;

    }

    private void sendNotification(Friend friend, String text) {

        EventNotification eventNotification = new EventNotification(friend.getFromAccountId(), friend.getToAccountId(), NotificationType.FRIEND_REQUEST, text);
        eventNotificationKafkaTemplate.send("event-notification", eventNotification);

    }

    private List<Friend> getCurrentInvoicesByAccountId(Long id) {

        List<Friend> list = getOutgoingInvoice(id);
        list.addAll(getIncomingInvoice(id));
        return list;

    }

    private List<Friend> getOutgoingInvoice(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setIdFrom(getJwtUserIdFromSecurityContext());
        searchDto.setIdTo(id);
        return friendRepository.findAll(getSpecification(searchDto));

    }

    private List<Friend> getIncomingInvoice(Long id) {

        FriendSearchDto searchDto = new FriendSearchDto();
        searchDto.setIdFrom(id);
        searchDto.setIdTo(getJwtUserIdFromSecurityContext());
        return friendRepository.findAll(getSpecification(searchDto));

    }

    public List<Long> getFriendsIds() {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setStatusCode(StatusCode.FRIEND);
        friendSearchDto.setIdFrom(getJwtUserIdFromSecurityContext());
        return friendRepository.findAll(getSpecification(friendSearchDto)).stream().map(Friend::getToAccountId).collect(Collectors.toList());

    }

    public List<Long> getFriendsIds(Long id) {
        return friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.FRIEND)))
                .stream()
                .map(Friend::getToAccountId)
                .collect(Collectors.toList());

    }

    private void blockIfBlocked(Long id) {

        if (getOutgoingInvoice(id).isEmpty()) {
            friendRepository.save(new Friend(getJwtUserIdFromSecurityContext(), StatusCode.BLOCKED, id));
        } else if (getOutgoingInvoice(id).get(0).getStatusCode().equals(StatusCode.BLOCKED)) {
            friendRepository.deleteAll(getOutgoingInvoice(id));
        }
    }


    private void blockIfNoConnection(Long id) {

        friendRepository.save(new Friend(getJwtUserIdFromSecurityContext(), StatusCode.BLOCKED, id));
        removeRecommendations();
        createRecommendations();

    }


    public StatusCode getStatus(Long id) {
        List<Friend> friend = getOutgoingInvoice(id);
        if (friend.isEmpty()) {
            return StatusCode.NONE;
        }
        return friend.get(0).getStatusCode();
    }


    private Boolean checkIfImBlocked(Long id) {

        return !friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.BLOCKED, getJwtUserIdFromSecurityContext()))).isEmpty();

    }

    public List<Long> getBlockedFriendsIds() {

        return friendRepository.findAll(getSpecification(new FriendSearchDto(getJwtUserIdFromSecurityContext(), StatusCode.BLOCKED)))
                .stream()
                .map(Friend::getToAccountId)
                .collect(Collectors.toList());
    }

    public List<Long> getBlockedFriendsIds(Long id) {

        return friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.BLOCKED)))
                .stream()
                .map(Friend::getToAccountId)
                .collect(Collectors.toList());
    }


    //
    //=================================================RECOMMENDATIONS==================================================
    //

    public void createRecommendations() {

        log.info("FriendService in createRecommendations tried to create new Recommendations");
        List<Long> allIds = friendRepository.findAll(getSpecification(new FriendSearchDto(StatusCode.FRIEND)))
                .stream()
                .map(Friend::getFromAccountId)
                .distinct()
                .toList();

        for (Long id : allIds) {

            List<Long> exceptIds = getFriendsIdsWhoBlocked(id);
            exceptIds.addAll(getFriendsIdsWhoBlockedByMe(id));
            exceptIds.addAll(getRequestsTo(id));
            exceptIds.addAll(getRequestsFrom(id));
            exceptIds.add(id);
            List<Long> friendIds = getFriendsIds(id);
            Map<Long, Long> friendsFriendsId = new TreeMap<>();
            for (Long friendId : friendIds) {
                if (!exceptIds.contains(friendId)) {
                    List<Long> list = getFriendsIds(friendId);
                    for (Long f : list) {
                        if (friendIds.contains(f) || exceptIds.contains(f)) continue;
                        if (friendsFriendsId.containsKey(f)) {
                            friendsFriendsId.put(f, friendsFriendsId.get(f) + 1L);
                        } else {
                            friendsFriendsId.put(f, 1L);
                        }
                    }
                }
            }

            log.info("RECOMMENDATIONS MAP" + friendsFriendsId);

            List<Long> resultList = friendsFriendsId.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(10)
                    .map(Map.Entry::getKey)
                    .toList();

            Long count = 0L;

            for (Long entry : resultList) {
                count++;
                friendRepository.deleteAll(friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.RECOMMENDATION, entry))));
                Friend friend = new Friend(id, StatusCode.RECOMMENDATION, entry, count);
                friendRepository.save(friend);
            }

            log.info("RECOMMENDATIONS LIST" + resultList);
        }
    }

    public void removeRecommendations() {

        log.info("FriendService in removeRecommendations tried to remove Recommendations");
        friendRepository.deleteAll(friendRepository.findAll(getSpecification(new FriendSearchDto(StatusCode.RECOMMENDATION))));
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


    public List<Long> getFriendsIdsWhoBlocked(Long id) {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIdFrom(id);
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getToAccountId).collect(Collectors.toList());
    }

    public List<Long> getFriendsIdsWhoBlockedByMe(Long id) {
        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIdTo(id);
        friendSearchDto.setStatusCode(StatusCode.BLOCKED);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getFromAccountId).collect(Collectors.toList());
    }

    public List<Long> getRequestsTo(Long id) {

        return friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.REQUEST_TO)))
                .stream()
                .map(Friend::getToAccountId)
                .collect(Collectors.toList());

    }

    public List<Long> getRequestsFrom(Long id) {

        FriendSearchDto friendSearchDto = new FriendSearchDto();
        friendSearchDto.setIdTo(id);
        friendSearchDto.setStatusCode(StatusCode.REQUEST_TO);
        return (friendRepository.findAll(getSpecification(friendSearchDto))).stream().map(Friend::getFromAccountId).collect(Collectors.toList());

    }

    private List<Friend> getRecommendationsFromDB(Long id) {

        return friendRepository.findAll(getSpecification(new FriendSearchDto(id, StatusCode.RECOMMENDATION)));

    }

    //
    //=================================================SPECIFICATION====================================================
    //

    private Specification<Friend> getSpecification(FriendSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(Friend_.id, searchDto.getIds(), true))
                .and(equal(Friend_.statusCode, searchDto.getStatusCode(), true)
                        .and(equal(Friend_.fromAccountId, searchDto.getIdFrom(), true))
                        .and(equal(Friend_.toAccountId, searchDto.getIdTo(), true)));
    }

}
