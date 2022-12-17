package ru.skillbox.diplom.group32.social.service.repository.base;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@NoRepositoryBean
public class BaseRepositoryImpl<Entity extends BaseEntity>
        extends SimpleJpaRepository<Entity, Long>
        implements BaseRepository<Entity> {

    EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<Entity, Long> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        super.findById(id).ifPresent(x -> {
            x.setIsDeleted(true);
            super.save(x);
        });
    }

    @Transactional
    @Override
    public void delete(Entity entity) {
        entity.setIsDeleted(true);
        super.save(entity);
    }

    @Transactional
    public void deleteAll(Iterable<? extends Entity> entities) {
        entities.forEach(entity -> entity.setIsDeleted(true));
        super.saveAll(entities);
    }

    @Transactional
    @Override
    public void hardDeleteById(Long id) {
        super.findById(id).ifPresent(super::delete);
    }

    @Transactional
    @Override
    public void hardDelete(Entity entity) {
        super.delete(entity);
    }

    @Transactional
    @Override
    public Optional<Entity> findById(Long id) {
        if (super.findById(id).isEmpty()){
            throw new EntityNotFoundException();
        }
        return super.findById(id);
    }

}
