package ru.skillbox.diplom.group32.social.service.service.dialog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.dialog.DialogMapper;
import ru.skillbox.diplom.group32.social.service.mapper.dialog.message.MessageMapper;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogSearchDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog_;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.*;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.DialogsRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.MessagesRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.StatusMessageReadRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.UnreadCountRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.setStatusMessageDto.SetStatusMessageReadDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.unreadCountDto.UnreadCountDto;
import ru.skillbox.diplom.group32.social.service.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group32.social.service.repository.dialog.message.MessageRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

import javax.persistence.criteria.Join;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogService {
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final AccountService accountService;
    private final DialogMapper dialogMapper;
    private final MessageMapper messageMapper;

    public DialogsRs getAllDialogs(Long offset, Long itemPerPage, Pageable page) {

        DialogsRs response = new DialogsRs("", "", getTimestamp());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setTotal(dialogRepository.count());
        response.setCurrentUserId(getJwtUserIdFromSecurityContext());

        DialogSearchDto searchDto = new DialogSearchDto();
        searchDto.setIsDeleted(false);
        searchDto.setLastMessageAuthorId(getJwtUserIdFromSecurityContext());

        log.info("DialogService in getAllDialogs tried to find all dialog with DialogSearchDto: " + searchDto);

        List<Dialog> dialogList = dialogRepository.findAll(getDialogSpecification(searchDto));

        List<DialogDto> dtoList = dialogList.stream().map(dialog -> {
            Long conversationPartnerId = dialog.getLastMessage().getAuthorId()
                    .equals(getJwtUserIdFromSecurityContext())
                    ? dialog.getLastMessage().getRecipientId()
                    : dialog.getLastMessage().getAuthorId();
            DialogDto dto = dialogMapper.convertToDto(dialog);
            dto.setConversationPartner(accountService.getAccountById(conversationPartnerId));
            dto.setUnreadCount(getUnreadMessageCountCurrentUser());
            return dto;
        }).toList();

        response.setData(dtoList);

        return response;

    }

    public UnreadCountRs getUnreadMessageCount() {

        log.info("DialogService in getUnreadMessageCount: trying to get unread message count with id: " + getJwtUserIdFromSecurityContext());
        UnreadCountRs response = new UnreadCountRs("", "", getTimestamp());
        response.setData(new UnreadCountDto(getUnreadMessageCountCurrentUser()));

        return response;

    }

    public MessagesRs getAllMessages(Long recipientId, Long offset, Long itemPerPage, Pageable page) {

        MessagesRs response = new MessagesRs("", "", getTimestamp());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setTotal(messageRepository.count());

        MessageSearchDto searchDto = createMessageSearch(recipientId, null);

        log.info("DialogService in getAllMessages: trying to get all messages count with MessageSearchDto: " + searchDto);

        List<Message> messageList = messageRepository.findAll(getMessageSpecification(searchDto));
        response.setData(messageList.stream().map(messageMapper::convertToMessageShortDto).toList());
        return response;

    }

    public StatusMessageReadRs setStatusMessageRead(Long companionId) {

        MessageSearchDto searchDto = createMessageSearch(companionId, ReadStatusDto.SENT);
        List<Message> messageList = messageRepository.findAll(getMessageSpecification(searchDto));

        messageList.forEach(message -> {
            if (message.getRecipientId().equals(getJwtUserIdFromSecurityContext())) {
                message.setReadStatus(ReadStatus.READ);
                log.info("DialogService in setStatusMessageRead: trying to set READ status for message with id: " + message.getId());
            }
        });
        messageRepository.saveAll(messageList);

        StatusMessageReadRs response = new StatusMessageReadRs("", "", getTimestamp());
        return response.setData(new SetStatusMessageReadDto("ok"));

    }

    public MessageDto createMessage(MessageDto dto) {

        dto.setReadStatusDto(ReadStatusDto.SENT);
        Message message = messageRepository.save(messageMapper.convertToEntity(dto));

        log.info("DialogService in createMessage: message was create with id: " + message.getId());

        Dialog dialog = getDialog(dto.getAuthorId(), dto.getRecipientId());
        dialog.setLastMessage(message);
        dialogRepository.save(dialog);

        return messageMapper.convertToDto(message);

    }

    private Long getUnreadMessageCountCurrentUser() {
        return messageRepository.countByRecipientIdAndReadStatus(
                getJwtUserIdFromSecurityContext(),
                ReadStatus.SENT);
    }
    private Dialog getDialog(Long authorId, Long companionId) {

        DialogSearchDto searchDto = new DialogSearchDto();
        searchDto.setIsDeleted(false);
        searchDto.setLastMessageAuthorId(authorId);
        searchDto.setLastMessageRecipientId(companionId);

        List<Dialog> dialogList = dialogRepository.findAll(getDialogSpecification(searchDto));

        if (dialogList.size() == 0) {
            Dialog dialog = new Dialog();
            dialog.setIsDeleted(false);

            log.info("DialogService in getDialog: dialog was create with authorId: " + authorId + " and companionId " + companionId);

            return dialog;
        }

        return dialogList.get(0);
    }

    private Specification<Message> getMessageSpecification(MessageSearchDto searchDto) {

        return getBaseSpecification(searchDto)
                .and((equal(Message_.authorId, searchDto.getAuthorId(), true)
                        .and(equal(Message_.recipientId, searchDto.getRecipientId(), true)))
                        .or(equal(Message_.authorId, searchDto.getRecipientId(), true)
                                .and(equal(Message_.recipientId, searchDto.getAuthorId(), true))))
                .and(equal(Message_.readStatus, messageMapper.convertReadStatusToEntity(searchDto.getReadStatus()), true));

    }

    private Specification<Dialog> getDialogSpecification(DialogSearchDto searchDto) {

        return getBaseSpecification(searchDto)
                .and((containsLastMessageAuthor(searchDto.getLastMessageAuthorId())
                        .and(containsLastMessageRecipient(searchDto.getLastMessageRecipientId())))
                        .or((containsLastMessageAuthor(searchDto.getLastMessageRecipientId())
                                .and(containsLastMessageRecipient(searchDto.getLastMessageAuthorId())))));

    }

    private static Specification<Dialog> containsLastMessageAuthor(Long userId) {

        return nullValueCheck(userId, true, () -> {
            return (root, query, builder) -> {

                Join<Dialog, Message> join = root.join(Dialog_.lastMessage);
                return builder.equal(join.get(Message_.authorId), userId);

            };
        });

    }

    private static Specification<Dialog> containsLastMessageRecipient(Long userId) {

        return nullValueCheck(userId, true, () -> {
            return (root, query, builder) -> {

                Join<Dialog, Message> join = root.join(Dialog_.lastMessage);
                return builder.equal(join.get(Message_.recipientId), userId);

            };
        });

    }

    private Long getTimestamp() {

        return ZonedDateTime.now().toInstant().toEpochMilli();

    }

    private MessageSearchDto createMessageSearch(Long recipientId, ReadStatusDto status) {

        MessageSearchDto searchDto = new MessageSearchDto();
        searchDto.setAuthorId(getJwtUserIdFromSecurityContext());
        searchDto.setRecipientId(recipientId);
        searchDto.setIsDeleted(false);
        searchDto.setReadStatus(status);
        return searchDto;

    }
}
