package ru.skillbox.diplom.group32.social.service.model.base;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", schema = "social_service", allocationSize = 100)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}
