package ru.skillbox.diplom.group32.social.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByAgeAndName(int age, String name);

}
