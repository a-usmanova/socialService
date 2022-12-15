package ru.skillbox.diplom.group32.social.service.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.tag.TagMapper;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;
import ru.skillbox.diplom.group32.social.service.model.tag.TagSearchDto;
import ru.skillbox.diplom.group32.social.service.model.tag.Tag_;
import ru.skillbox.diplom.group32.social.service.repository.tag.TagRepository;

import java.util.*;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagDto getById(Long id) {

        log.info("TagService in getById tried to find tag with id: " + id);
        return tagMapper.convertToDto(tagRepository.findById(id).orElseThrow(ObjectNotFoundException::new));

    }

    public List<TagDto> getAll(TagSearchDto searchDto) {
        return tagMapper.convertToDtoList(tagRepository.findAll(getSpecification(searchDto)));
    }

    //*TODO брать за пример
    public TagDto create(TagDto tagDto) {
        log.info("Tag to save - " + tagDto);
        Tag tag = tagRepository.save(tagMapper.convertToEntity(tagDto));
        log.info("Tag saved to db - " + tagDto);

        return tagMapper.convertToDto(tag);
    }

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
        tagRepository.saveAll(tags);
        return tagSet;
    }

    public TagDto update(TagDto tagDto) {
        Tag tag = tagRepository.save(tagMapper.convertToEntity(tagDto));
        log.info("TagService in updateTag: Tag updated. New Tag: " + tagDto);

        return tagMapper.convertToDto(tag);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    public void deleteAll(Set<Tag> tags) {
        Set<Tag> tagToDelete = checkForDelete(tags);
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
            isDelete = !posts.stream().filter(t -> t.getIsDeleted() != true).findFirst().isPresent();
            if (isDelete) {
                tagSet.add(tag);
            }
        });
        return tagSet;
    }
    private static Specification<Tag> getSpecification(TagSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(equal(Tag_.id, searchDto.getId(), true))
                .and(like(Tag_.name, searchDto.getName(), true));
    }
}
