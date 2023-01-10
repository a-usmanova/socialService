package ru.skillbox.diplom.group32.social.service.repository.auth.passwordRecovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.PasswordRecovery;

import java.util.UUID;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, UUID> {
}
