package ru.skillbox.diplom.group32.social.service.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.account.AccountController;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {


    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<AccountDto> get() {
        return new ResponseEntity<>(accountService.getAccount(), HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> delete() {
        return new ResponseEntity<>(accountService.softDeleteAccount(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountDto> getById(Long id) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<Page<List<AccountDto>>> getAll(AccountSearchDto searchDto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<AccountDto> create(AccountDto dto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping("/me")
    @Override
    public ResponseEntity<AccountDto> update(AccountDto accountDto) {
        return new ResponseEntity<>(accountService.updateAccount(accountDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

}
