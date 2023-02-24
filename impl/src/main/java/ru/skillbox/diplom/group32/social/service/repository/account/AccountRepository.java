package ru.skillbox.diplom.group32.social.service.repository.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface AccountRepository extends BaseRepository<Account> {
    @Query(value = "SELECT account.id FROM account WHERE extract(MONTH FROM account.birth_date) = :month AND extract(DAY FROM account.birth_date) = :day", nativeQuery = true)
    List<Long> getAccountsByBirthDateMonthAndDay(Integer month, Integer day);
}
