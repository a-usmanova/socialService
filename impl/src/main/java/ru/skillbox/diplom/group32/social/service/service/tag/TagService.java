package ru.skillbox.diplom.group32.social.service.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.repository.tag.TagRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Set<Tag> createNonExistent(Set<String> tagNames) {
        List<Tag> tags = tagRepository.findByNameIn(tagNames);
        tags.forEach(e->e.setIsDeleted(false));

        Set<Tag> tagSet = new HashSet<>(tags);
        tagNames.forEach(tagName->{
            Tag newTag = new Tag();
            newTag.setName(tagName);
            if (!tagSet.contains(newTag)) {
                newTag.setIsDeleted(false);
                tagSet.add(newTag);
            }
        });
        log.info("Tags saved - " + tags);
        tagRepository.saveAll(tags);
        return tagSet;
    }

    public void deleteAll(Set<Tag> tags) {
        Set<Tag> tagToDelete = checkForDelete(tags);
        log.info("Tags - " + tagToDelete + " soft deleted.");
        tagRepository.deleteAll(tagToDelete);
    }

    public static Set<String> getNames(Set<Tag> tagSet) {
        Set<String> names = new HashSet<>();
        tagSet.forEach(tagDto -> names.add(tagDto.getName()));
        return names;
    }

    private Set<Tag> checkForDelete(Set<Tag> tags) {
        Set<Tag> tagSet = new HashSet<>();
        tags.forEach(tag -> {
            boolean isDelete;
            Set<Post> posts = tag.getPosts();
            isDelete = !posts.stream().anyMatch(t -> !t.getIsDeleted());
            if (isDelete) {
                tagSet.add(tag);
            }
        });
        return tagSet;
    }

}
