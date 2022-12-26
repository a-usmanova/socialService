package ru.skillbox.diplom.group32.social.service.service.post;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.config.Properties;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group32.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group32.social.service.model.post.*;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag_;
import ru.skillbox.diplom.group32.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group32.social.service.service.tag.TagService;

import javax.persistence.criteria.Join;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private Properties properties;

    public PostDto getPostById(Long id) {

        log.info("PostService in getById tried to find post with id: " + id);
        return postMapper.convertToDto(postRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<PostDto> getAll(PostSearchDto searchDto, Pageable page) {

        Page<Post> postPage = postRepository.findAll(getSpecification(searchDto), page);
        return postPage.map(e->{
            PostDto postDto = postMapper.convertToDto(e);
            postDto.setTags(tagService.getNames(e.getTags()));
            return postDto;
        });

    }

    //*TODO брать за пример
    public PostDto create(PostDto postDto) {
        postDto.setAuthorId(getJwtUserIdFromSecurityContext());
        postDto.setIsDeleted(false);
        postDto.setTime(ZonedDateTime.now());
        postDto.setPublishDate(ZonedDateTime.now());
        postDto.setMyLike(false);
        postDto.setCommentsCount(0L);
        postDto.setLikeAmount(0L);
        postDto.setIsBlocked(false);
        postDto.setType(Type.POSTED);
        postDto.setTimeChanged(ZonedDateTime.now());
        log.info("Post to save - " + postDto);

        Post postEntity = postMapper.convertToEntity(postDto);
        postEntity.setTags(tagService.createNonExistent(postDto.getTags()));
        Post post = postRepository.save(postEntity);
        log.info("Post saved to db - " + postDto);

        return postMapper.convertToDto(post);
    }

    public PostDto update(PostDto postDto) {

        Post post = postRepository.save(postMapper.convertToEntity(postDto));
        log.info("PostService in updatePost: Post updated. New Post: " + postDto);

        return postMapper.convertToDto(post);
    }

    public void deleteById(Long id) {
        log.info("User ID to del - " + id);
        postRepository.deleteById(id);
        Post post = postRepository.findById(id).get();
        tagService.deleteAll(post.getTags());
    }

    private Specification<Post> getSpecification(PostSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(in(Post_.id, searchDto.getIds(), true))
                .and(in(Post_.authorId, searchDto.getAccountIds(), true))
                .and(notIn(Post_.authorId, searchDto.getBlockedIds(), true))
// в Post нет author --- .and(equal(Post_.author, searchDto.getAuthor(), true)
                .and(equal(Post_.title, searchDto.getTitle(), true)
                        .and(equal(Post_.postText, searchDto.getPostText(), true)
// в Post нет withFriends --- .and(equal(Post_.withFriends, searchDto.getWithFriends(), true)
// в Post нет tags --- .and(in(Post_.tags, Arrays.stream(searchDto.getTags()).toList(), true))
                                .and(between(Post_.publishDate, searchDto.getDateFrom(), searchDto.getDateTo(), true))
                                .and(containsTag(searchDto.getTags()))));
    }

    public String savePhoto(MultipartFile request) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", properties.getCloudName(),
                "api_key", properties.getApiKey(),
                "api_secret", properties.getApiSecret()));

        Map uploadResult = cloudinary.uploader().upload(request.getBytes(), ObjectUtils.emptyMap());
        log.info("successfully uploaded the file" + uploadResult.get("name"));

        return uploadResult.get("url").toString();
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