package ru.skillbox.diplom.group32.social.service.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public void createAccount(User user) {
        Account account = accountMapper.userToAccount(user);
        account.setIsBlocked(false);
        account.setRegDate(ZonedDateTime.now());
        account.setCreatedOn(ZonedDateTime.now());
        account.setUpdatedOn(ZonedDateTime.now());

        accountRepository.save(account);
        log.info("Account saved - " + account);
    }

    public AccountDto getAccount() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();

        Account account = accountRepository.findById(userId).get();
        log.info("Load Account from DB - " + account);
        return accountMapper.convertToDto(account);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        Account accountFromDB = accountRepository.save(accountMapper.convertToAccount(accountDto));
        log.info("Updated Account is - " + accountFromDB);
        return accountMapper.convertToDto(accountFromDB);
    }

    public String softDeleteAccount() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        accountRepository.deleteById(userId);
        log.info("Account with id - " + userId + " soft deleted.");
        return "Account with id - " + userId + " deleted.";
    }

}
