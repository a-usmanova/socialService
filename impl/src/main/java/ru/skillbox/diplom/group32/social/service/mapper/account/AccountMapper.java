package ru.skillbox.diplom.group32.social.service.mapper.account;

import org.mapstruct.*;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto convertToDto(Account account);

    Account convertToAccount(AccountDto accountDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account convertToAccount(AccountDto accountDto, @MappingTarget Account account);

    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "regDate", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "createdOn", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "updatedOn", expression = "java(ZonedDateTime.now())")
    Account userToAccount(User user);

}
