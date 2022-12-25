package ru.skillbox.diplom.group32.social.service.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.model.account.Account_;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public void createAccount(User user) {
        Account account = accountMapper.userToAccount(user);

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

    public Page<AccountDto> searchAccountByFirstAndLastName(AccountSearchDto accountSearchDto, Pageable page) {
        Page<Account> accountSearchPages = accountRepository.findAll(getSpecification(accountSearchDto), page);
        return accountSearchPages.map(accountMapper::convertToDto);
    }

    private static Specification<Account> getSpecification(AccountSearchDto accountSearchDto) {
        return getBaseSpecification(accountSearchDto)
                .or(like(Account_.firstName, accountSearchDto.getAuthor(), true))
                .or(like(Account_.lastName, accountSearchDto.getAuthor(), true));
    }

}
