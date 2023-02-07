package ru.skillbox.diplom.group32.social.service.service.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.model.account.Account_;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.auth.User_;
import ru.skillbox.diplom.group32.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group32.social.service.service.friend.FriendService;
import ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
//@RequiredArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private FriendService friendService;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, @Lazy FriendService friendService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.friendService = friendService;
    }

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

    public AccountDto getAccountById(Long userId) {
        Account account = accountRepository.findById(userId).get();
        log.info("Load Account from DB - " + account);
        return accountMapper.convertToDto(account);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();

        Account accountToSave = accountMapper.convertToAccount(accountDto, accountRepository.findById(userId).orElse(new Account()));
        accountRepository.save(accountToSave);
        log.info("Updated Account is - " + accountToSave);
        return accountMapper.convertToDto(accountToSave);
    }

    public String softDeleteAccount() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        accountRepository.deleteById(userId);
        log.info("Account with id - " + userId + " soft deleted.");
        return "Account with id - " + userId + " deleted.";
    }

    public List<AccountDto> searchByIds(AccountSearchDto accountSearchDto) {
        List<Account> resultAccount = accountRepository.findAll(getSpecificationByAccountIds(accountSearchDto));
        return resultAccount.stream().map(accountMapper::convertToDto).collect(Collectors.toList());
    }

    public Page<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable page) {
        Long currentUser = SecurityUtil.getJwtUserIdFromSecurityContext();
        List<Long> blockedByIds = friendService.getBlockedFriendsIds(currentUser);
        if (blockedByIds.size() != 0) {
            accountSearchDto.setBlockedByIds(blockedByIds);
        }

        Page<Account> accountSearchPages;
        if (accountSearchDto.getAuthor() != null) {
            accountSearchPages = accountRepository.findAll(getSpecificationByAuthor(accountSearchDto), page);
        } else {
            accountSearchPages = accountRepository.findAll(getSpecificationByOtherFields(accountSearchDto), page);
        }

        accountSearchPages.forEach(acc -> acc.setStatusCode(friendService.getStatus(acc.getId())));

        return accountSearchPages.map(accountMapper::convertToDto);
    }

    private static Specification<Account> getSpecificationByAccountIds(AccountSearchDto accountSearchDto) {
        ArrayList<String> notInList = new ArrayList<>();
        notInList.add(SecurityUtil.getJwtUserFromSecurityContext().getEmail());
        return getBaseSpecification(accountSearchDto)
                .and(notIn(User_.email, notInList, true))
                .and(in(Account_.id, accountSearchDto.getIds(), true));
    }

    private static Specification<Account> getSpecificationByAuthor(AccountSearchDto accountSearchDto) {
        ArrayList<String> notInList = new ArrayList<>();
        notInList.add(SecurityUtil.getJwtUserFromSecurityContext().getEmail());
        return getBaseSpecification(accountSearchDto)
                .and(notIn(Account_.id, accountSearchDto.getBlockedByIds(), true))
                .and(likeLowerCase(Account_.firstName, accountSearchDto.getAuthor(), true)
                        .and(notIn(User_.email, notInList, true))
                        .or(likeLowerCase(Account_.lastName, accountSearchDto.getAuthor(), true)
                                .and(notIn(User_.email, notInList, true))));
    }

    private static Specification<Account> getSpecificationByOtherFields(AccountSearchDto accountSearchDto) {
        ArrayList<String> notInList = new ArrayList<>();
        notInList.add(SecurityUtil.getJwtUserFromSecurityContext().getEmail());
        return getBaseSpecification(accountSearchDto)
                .and(notIn(Account_.id, accountSearchDto.getBlockedByIds(), true))
                .and(in(Account_.id, accountSearchDto.getIds(), true))
                .and(likeLowerCase(Account_.firstName, accountSearchDto.getFirstName(), true)
                        .and(likeLowerCase(Account_.lastName, accountSearchDto.getLastName(), true)
                                .and(notIn(User_.email, notInList, true))))
                .and(equal(Account_.country, accountSearchDto.getCountry(), true))
                .and(equal(Account_.city, accountSearchDto.getCity(), true))
                .and(between(Account_.birthDate,
                        accountSearchDto.getAgeTo() == null ? null : ZonedDateTime.now().minusYears(accountSearchDto.getAgeTo()),
                        accountSearchDto.getAgeFrom() == null ? null : ZonedDateTime.now().minusYears(accountSearchDto.getAgeFrom()), true));
    }

    public AccountDto updateAccountPhoto(Map uploadResult) {
        AccountDto resultAccountDto = getAccount();
        resultAccountDto.setPhoto(uploadResult.get("url").toString());
        resultAccountDto.setPhotoName(uploadResult.get("original_filename").toString());
        updateAccount(resultAccountDto);
        return resultAccountDto;
    }

}
