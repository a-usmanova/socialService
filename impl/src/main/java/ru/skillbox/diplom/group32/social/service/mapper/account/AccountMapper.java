package ru.skillbox.diplom.group32.social.service.mapper.account;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto convertToDto(Account account);

    Account convertToAccount(AccountDto accountDto);

    Account userToAccount(User user);
}
