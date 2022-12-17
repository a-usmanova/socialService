package ru.skillbox.diplom.group32.social.service.resource.account;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.account.AccountSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;

@RequestMapping("api/v1/account")
public interface AccountController extends BaseController<AccountDto, AccountSearchDto> {
}
