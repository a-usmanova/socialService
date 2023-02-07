package ru.skillbox.diplom.group32.social.service.resource.dialog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.DialogsRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.MessagesRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.StatusMessageReadRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.UnreadCountRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.setStatusMessageDto.SetStatusMessageReadDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.unreadCountDto.UnreadCountDto;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

@RequestMapping(value = WebConstant.VERSION_URL  + "/dialogs")
public interface DialogController {

    @GetMapping(value = "/unreaded")
    ResponseEntity<UnreadCountRs> getUnreadMessageCount();

    @GetMapping
    ResponseEntity<DialogsRs> getAllDialogs(@RequestParam(defaultValue = "0") Long offset,
                                            @RequestParam(defaultValue = "20") Long itemPerPage, Pageable page);

    @GetMapping(value = "/messages")
    ResponseEntity<MessagesRs> getAllMessages(@RequestParam Long companionId,
                                              @RequestParam(defaultValue = "0") Long offset,
                                              @RequestParam(defaultValue = "20") Long itemPerPage, Pageable page);

    @PutMapping(value = "/{companionId}")
    ResponseEntity<StatusMessageReadRs> setStatusMessageRead(@PathVariable Long companionId);
}
