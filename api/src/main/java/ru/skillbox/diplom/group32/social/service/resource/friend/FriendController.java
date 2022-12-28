package ru.skillbox.diplom.group32.social.service.resource.friend;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendDto;
import ru.skillbox.diplom.group32.social.service.model.friend.FriendSearchDto;
import ru.skillbox.diplom.group32.social.service.model.friend.RecommendationDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import java.util.List;

@Tag(name = "Friend service", description = "Сервис друзей")
@RequestMapping(value = WebConstant.VERSION_URL  + "/friends")
public interface FriendController extends BaseController<FriendDto, FriendSearchDto> {

    @GetMapping(value = "/{id}")
    ResponseEntity<FriendDto> getById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto searchDto, Pageable page);

    @GetMapping(value = "/recommendations")
    ResponseEntity<Page<RecommendationDto>> getRecommendation(FriendSearchDto searchDto);

    @GetMapping(value = "/friendId")
    ResponseEntity<List<Long>> getFriendId();

    @GetMapping(value = "/count")
    ResponseEntity count();

    @GetMapping(value = "/blockFriendId")
    ResponseEntity<List<Long>> getBlockedFriendsIds();

    @PostMapping(value = "{id}/request")
    ResponseEntity addFriend(@PathVariable Long id);

    @PostMapping(value = "subscribe/{id}")
    ResponseEntity addSubscription(@PathVariable Long id);

    @PutMapping(value = "{id}/approve")
    ResponseEntity approveFriend(@PathVariable Long id);

    @PutMapping(value = "block/{id}")
    ResponseEntity blockFriend(@PathVariable Long id);

    @DeleteMapping(value = "/{id}")
    ResponseEntity deleteById(@PathVariable Long id);

}
