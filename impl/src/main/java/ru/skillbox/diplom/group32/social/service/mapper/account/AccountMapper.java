package ru.skillbox.diplom.group32.social.service.mapper.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto convertToDto(Account account);

    Account convertToAccount(AccountDto accountDto);

    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "regDate", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "createdOn", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "updatedOn", expression = "java(ZonedDateTime.now())")
    Account userToAccount(User user);

}
