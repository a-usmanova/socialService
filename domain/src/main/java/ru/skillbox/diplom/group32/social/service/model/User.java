package ru.skillbox.diplom.group32.social.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "user", schema = "social_service")
public class User extends BaseEntity {

    public User(String name, Integer age) {
        super.setIsDeleted(false);
        this.name = name;
        this.age = age;
    }

    public User(Long id,  String name, Integer age) {
        super.setId(id);
        this.name = name;
        this.age = age;
    }

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(columnDefinition = "INTEGER", nullable = false)
    private Integer age;


}
