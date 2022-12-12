package ru.skillbox.diplom.group32.social.service.repository.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", source = "id")
    AccountDto AccountToDto(Account account);

    Account DtoToAccount(AccountDto accountDto);

}
