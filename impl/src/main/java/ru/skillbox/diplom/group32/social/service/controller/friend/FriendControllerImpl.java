package ru.skillbox.diplom.group32.social.service.controller.friend;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.friend.FriendController;
import ru.skillbox.diplom.group32.social.service.service.friend.FriendService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendControllerImpl implements FriendController {

    private final FriendService friendService;
    @Override
    public ResponseEntity<FriendDto> getById(Long id) {
        return ResponseEntity.ok(friendService.getById(id));
    }

    @Override
    public ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto searchDto, Pageable page) {
        return ResponseEntity.ok(friendService.getAll(searchDto, page));
    }

    @Override
    public ResponseEntity<List<FriendDto>> getRecommendation(FriendSearchDto searchDto) {
        return ResponseEntity.ok(friendService.getRecommendation());
    }

    @Override
    public ResponseEntity<List<Long>> getFriendId() {
        return ResponseEntity.ok(friendService.getFriendsIds());
    }

    @Override
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(friendService.getCount());
    }

    @Override
    public ResponseEntity<List<Long>> getBlockedFriendsIds() {
        return ResponseEntity.ok(friendService.getBlockedFriendsIds());
    }

    @Override
    public ResponseEntity<List<FriendDto>> addFriend(Long id) {
        return ResponseEntity.ok(friendService.addFriend(id));
    }

    @Override
    public ResponseEntity<List<FriendDto>> addSubscription(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> approveFriend(Long id)  {
        friendService.approveFriend(id);
        return ResponseEntity.ok().body("ok");
    }

    @Override
    public ResponseEntity<String> blockFriend(Long id) {
        friendService.blockFriend(id);
        return ResponseEntity.ok().body("ok");
    }

    @Override
    @Hidden
    public ResponseEntity<FriendDto> create(FriendDto dto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    @Hidden
    public ResponseEntity<FriendDto> update(FriendDto dto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<String> deleteById(Long id) {
        friendService.deleteById(id);
        return ResponseEntity.ok().body("FRIEND DELETED");
    }
}
