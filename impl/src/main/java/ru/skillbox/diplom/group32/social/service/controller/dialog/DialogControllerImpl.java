package ru.skillbox.diplom.group32.social.service.controller.dialog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.DialogsRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.MessagesRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.StatusMessageReadRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.UnreadCountRs;
import ru.skillbox.diplom.group32.social.service.resource.dialog.DialogController;
import ru.skillbox.diplom.group32.social.service.service.dialog.DialogService;

@RestController
@RequiredArgsConstructor
public class DialogControllerImpl implements DialogController {
    private final DialogService dialogService;

    @Override
    public ResponseEntity<UnreadCountRs> getUnreadMessageCount() {
        return ResponseEntity.ok(dialogService.getUnreadMessageCount());
    }

    @Override
    public ResponseEntity<DialogsRs> getAllDialogs(Integer offset, Integer itemPerPage) {
        return ResponseEntity.ok(dialogService.getAllDialogs(offset, itemPerPage));
    }

    @Override
    public ResponseEntity<MessagesRs> getAllMessages(Long companionId, Integer offset, Integer itemPerPage) {
        return ResponseEntity.ok(dialogService.getAllMessages(companionId, offset, itemPerPage));
    }

    @Override
    public ResponseEntity<StatusMessageReadRs> setStatusMessageRead(Long companionId) {
        return ResponseEntity.ok(dialogService.setStatusMessageRead(companionId));
    }
}
