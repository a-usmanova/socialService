package ru.skillbox.diplom.group32.social.service.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.like.LikeType;
import ru.skillbox.diplom.group32.social.service.model.notification.EventNotification;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.Post_;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag_;
import ru.skillbox.diplom.group32.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.friend.FriendService;
import ru.skillbox.diplom.group32.social.service.service.like.LikeService;
import ru.skillbox.diplom.group32.social.service.service.tag.TagService;

import javax.persistence.criteria.Join;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AccountService accountService;
    private final FriendService friendService;
    private final TagService tagService;
    private final PostMapper postMapper;
    private final LikeService likeService;
    private final KafkaTemplate<String, EventNotification> eventNotificationKafkaTemplate;

    public PostDto getById(Long id) {

        log.info("PostService in getById tried to find post with id: " + id);
        return postMapper.convertToDto(postRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<PostDto> getAll(PostSearchDto searchDto, Pageable page) {

        log.info("PostService in getAll tried to find posts with postSearchDto: {} and pageable: {}", searchDto, page);

        List<Long> listFriendsIds = friendService.getFriendsIds();
        listFriendsIds.add(getJwtUserIdFromSecurityContext());
        List<Long> listBlockedIds = friendService.getBlockedFriendsIds();
        if (!listBlockedIds.isEmpty()) {
        searchDto.setBlockedIds(listBlockedIds);}
        if (searchDto.getWithFriends() != null) {
            searchDto.setAccountIds(listFriendsIds);
        }

        if (searchDto.getAuthor() != null) {
            List<Long> listLong = new ArrayList<>();
            Page<AccountDto> pageAccount = accountService.searchAccount(postMapper.convertPostSearchDtoToAccountSearchDto(searchDto), PageRequest.of(0, 1000));
            for (AccountDto accountDto : pageAccount) {
                if (listFriendsIds.contains(accountDto.getId())){
                    listLong.add(accountDto.getId());
                }
            }
            searchDto.setAccountIds(listLong);
        }

        if (searchDto.getAccountIds() == null) {
            searchDto.setIds(postRepository.findAll(getSpecification(new PostSearchDto()))
                    .stream()
                    .map(Post::getId)
                    .collect(Collectors.toList()));
        }

        if (searchDto.getDateTo() == null) {
            searchDto.setDateTo(ZonedDateTime.now());
        }

        Page<Post> postPage = postRepository.findAll(getSpecification(searchDto), page);
        return new PageImpl<>(postPage.map(e -> {
            PostDto postDto = postMapper.convertToDto(e);
            postDto.setTags(tagService.getNames(e.getTags()));
            postDto.setMyLike(likeService.getMyLike(postDto.getId(), LikeType.POST));
            return postDto;
        }).toList(), page, postPage.getTotalElements());
    }

    public PostDto create(PostDto postDto) {

        log.info("PostService in create has post to save: " + postDto);
        Post postEntity = postMapper.convertToEntityCreated(postDto);
        postEntity.setTags(tagService.createNonExistent(postDto.getTags()));
        Post post = postRepository.save(postEntity);
        sendNotification(postEntity, "ОПУБЛИКОВАН НОВЫЙ ПОСТ");
        log.info("PostService in create: Post saved to db: " + postDto);

        return postMapper.convertToDto(post);
    }

    public PostDto update(PostDto postDto) {

        log.info("PostService in update has post with id: {} to update: {} ", postDto.getId(), postDto);
        Post post = updatePost(postDto);
        log.info("PostService in updatePost: Post updated. New Post: " + post);

        return postMapper.convertToDto(postRepository.save(post));
    }

    public void deleteById(Long id) {

        log.info("PostService in deleteById: trying to del post with id: " + id);
        postRepository.deleteById(id);
        Post post = postRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        tagService.deleteAll(post.getTags());

    }

    private void sendNotification(Post post, String text) {

        text = post.getTitle();
        EventNotification eventNotification = new EventNotification(post.getAuthorId(), null, NotificationType.POST, text.length() < 20 ? text : text.substring(0, 20) + "...");
        eventNotificationKafkaTemplate.send("event-notification", eventNotification);

    }

    private Specification<Post> getSpecification(PostSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(Post_.id, searchDto.getIds(), true))
                .and(in(Post_.authorId, searchDto.getAccountIds(), true))
                .and(notIn(Post_.authorId, searchDto.getBlockedIds(), true))
                                .and(between(Post_.publishDate, searchDto.getDateFrom(), searchDto.getDateTo(), true))
                                .and(containsTag(searchDto.getTags()));
    }


    private Post updatePost(PostDto postDto) {

        Post post = postRepository.findById(postDto.getId()).orElseThrow(ObjectNotFoundException::new);

        post.setTags(tagService.createNonExistent(postDto.getTags()));
        post.setTitle(postDto.getTitle());
        post.setPostText(postDto.getPostText());
        post.setImagePath(postDto.getImagePath());
        post.setTimeChanged(ZonedDateTime.now());
        post.setCommentsCount(postDto.getCommentsCount());

        return post;

    }

    private static Specification<Post> containsTag(Set<String> tags) {
        return (root, query, builder) -> {
            if (tags == null || tags.isEmpty()) {
                return builder.conjunction();
            }
            Join<Post, Tag> join = root.join(Post_.tags);
            return builder.in(join.get(Tag_.NAME)).value(tags);
        };
    }
}