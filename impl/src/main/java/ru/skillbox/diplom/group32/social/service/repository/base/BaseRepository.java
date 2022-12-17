package ru.skillbox.diplom.group32.social.service.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<Entity extends BaseEntity> extends JpaRepository<Entity, Long>, JpaSpecificationExecutor<Entity> {

    void delete(Entity entity);

    void deleteById(Long id);

    void deleteAll(Iterable<? extends Entity> entities);

    void hardDelete(Entity entity);

    void hardDeleteById(Long id);

    @Override
    Optional<Entity> findById(Long id);

}
