package ru.skillbox.diplom.group32.social.service.controller.account;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.account.AccountController;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    @Override
    @Hidden
    public ResponseEntity<AccountDto> getById(Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @Override
    @Hidden
    public ResponseEntity<Page<AccountDto>> getAll(AccountSearchDto searchDto, Pageable page) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    @Hidden
    public ResponseEntity<AccountDto> create(AccountDto dto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<AccountDto> update(AccountDto accountDto) {
        return new ResponseEntity<>(accountService.updateAccount(accountDto), HttpStatus.OK);
    }

    @Override
    @Hidden
    public ResponseEntity deleteById(Long id) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<AccountDto> getMe() {
        return new ResponseEntity<>(accountService.getAccount(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteMe() {
        return new ResponseEntity<>(accountService.softDeleteAccount(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<AccountDto>> search(AccountSearchDto accountSearchDto, Pageable page) {
        return new ResponseEntity<>(accountService.searchAccount(accountSearchDto, page), HttpStatus.OK);
    }
}
