package ru.skillbox.diplom.group32.social.service.model.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;
import ru.skillbox.diplom.group32.social.service.model.post.Post;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Table(name = "tag")
public class Tag extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
