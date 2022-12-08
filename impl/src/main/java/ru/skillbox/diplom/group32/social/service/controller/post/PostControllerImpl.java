package ru.skillbox.diplom.group32.social.service.controller.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.post.PostController;
import ru.skillbox.diplom.group32.social.service.service.post.PostService;

import java.util.List;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
public class PostControllerImpl implements PostController {

    final PostService postService;

    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public ResponseEntity<PostDto> getById(Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Override
    public ResponseEntity<Page<List<PostDto>>> getAll(PostSearchDto searchDto) {
        return new ResponseEntity(postService.getAllPosts(searchDto), HttpStatus.OK);
//*TODO поменять когда будет searchDto
    }

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostDto> create(PostDto dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostDto> update(PostDto dto) {
        return ResponseEntity.ok(postService.updatePost(dto));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        return null;
    }

    @PostMapping(value = "/storagePostPhoto", consumes = {MULTIPART_FORM_DATA_VALUE})
    public String storagePostPhoto (@RequestParam(value = "request", required = false) MultipartFile request) throws IOException {

        return postService.savePhoto(request);

    }

}
