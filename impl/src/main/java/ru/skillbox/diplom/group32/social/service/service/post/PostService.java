package ru.skillbox.diplom.group32.social.service.service.post;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.Post_;
import ru.skillbox.diplom.group32.social.service.repository.post.PostRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;


@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Value("${cloudinary.development.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.development.api_key}")
    private String apiKey;

    @Value("${cloudinary.development.api_secret}")
    private String apiSecret;

    public PostDto getPostById(Long id) {

        log.info("PostService in getById tried to find post with id: " + id);
        return postMapper.convertToDto(postRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public List<PostDto> getAllPosts(PostSearchDto searchDto) {
        return postMapper.convertToDtoList(postRepository.findAll(getSpecification(searchDto)));
    }

    //*TODO брать за пример
    public PostDto createPost(PostDto postDto) {
        log.info("Post to save - " + postDto);

        Post post = postRepository.save(postMapper.convertToEntity(postDto));
        log.info("Post saved to db - " + postDto);

        return postMapper.convertToDto(post);
    }

    public PostDto updatePost(PostDto postDto) {

        Post post = postRepository.save(postMapper.convertToEntity(postDto));
        log.info("PostService in updatePost: Post updated. New Post: " + postDto);

        return postMapper.convertToDto(post);
    }

    public static Specification<Post> getSpecification(PostSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(like(Post_.postText, searchDto.getPostText(), true)
                        .and(like(Post_.title, searchDto.getTitle(), true)
                                .and(equal(Post_.authorId, searchDto.getAuthorId(), true))));
    }

    public String savePhoto(MultipartFile request) throws IOException {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));

        Map uploadResult = cloudinary.uploader().upload(request.getBytes(), ObjectUtils.emptyMap());
        log.info("successfully uploaded the file" + request.getName());

        return uploadResult.get("url").toString();
    }

    public void deleteUserById(Long id) {
        log.info("User ID to del - " + id);
        postRepository.deleteById(id);
    }
}
