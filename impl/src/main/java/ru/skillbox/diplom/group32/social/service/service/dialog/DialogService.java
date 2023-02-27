package ru.skillbox.diplom.group32.social.service.service.dialog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.dialog.DialogMapper;
import ru.skillbox.diplom.group32.social.service.mapper.dialog.message.MessageMapper;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogSearchDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog_;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.*;
import ru.skillbox.diplom.group32.social.service.model.dialog.messageShortDto.MessageShortDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.DialogsRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.MessagesRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.StatusMessageReadRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.response.UnreadCountRs;
import ru.skillbox.diplom.group32.social.service.model.dialog.setStatusMessageDto.SetStatusMessageReadDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.unreadCountDto.UnreadCountDto;
import ru.skillbox.diplom.group32.social.service.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group32.social.service.repository.dialog.message.MessageRepository;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

import java.time.ZonedDateTime;
import java.util.*;

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

    public DialogsRs getAllDialogs(Integer offset, Integer itemPerPage) {

        Long authorId = getJwtUserIdFromSecurityContext();
        DialogsRs response = new DialogsRs("", "", getTimestamp());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setTotal(dialogRepository.count());
        response.setCurrentUserId(authorId);

        DialogSearchDto searchDto = new DialogSearchDto();
        searchDto.setIsDeleted(false);
        searchDto.setUserId(authorId);

        log.info("DialogService in getAllDialogs tried to find all dialog with DialogSearchDto: " + searchDto);

        List<Dialog> dialogList = dialogRepository.findAll(getDialogSpecification(searchDto)
                , PageRequest.of(offset, itemPerPage, Sort.by(Sort.Direction.DESC, "id"))).toList();
        HashMap<Long, AccountDto> accountHashMap = getAccountsDto(dialogList);

        List<DialogDto> dtoList = dialogList.stream().map(dialog -> {
            Long conversationPartnerId = dialog.getUserId1()
                    .equals(getJwtUserIdFromSecurityContext())
                    ? dialog.getUserId2()
                    : dialog.getUserId1();
            DialogDto dto = dialogMapper.convertToDto(dialog);
            dto.setConversationPartner(accountHashMap.get(conversationPartnerId));
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

    public MessagesRs getAllMessages(Long recipientId, Integer offset, Integer itemPerPage) {

        MessagesRs response = new MessagesRs("", "", getTimestamp());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setTotal(messageRepository.count());

        MessageSearchDto searchDto = getMessageSearchDto(getJwtUserIdFromSecurityContext(), recipientId, null);
        List<Message> messageList = messageRepository.findAll(getMessageSpecification(searchDto)
                    , PageRequest.of(offset, itemPerPage, Sort.by(Sort.Direction.DESC, "time"))).toList();

        log.info("DialogService in getAllMessages: trying to get all messages count with MessageSearchDto: " + searchDto);

        response.setData(messageList.stream().map(messageMapper::convertToMessageShortDto).toList());
        return response;

    }

    public StatusMessageReadRs setStatusMessageRead(Long companionId) {

        MessageSearchDto searchDto = getMessageSearchDto(getJwtUserIdFromSecurityContext(), companionId, ReadStatusDto.SENT);

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

    public MessageShortDto createMessage(MessageDto dto) {

        Dialog dialog = getDialog(dto.getAuthorId(), dto.getRecipientId());
        dto.setReadStatusDto(ReadStatusDto.SENT);
        dto.setDialogId(dialog.getId());

        Message message = messageRepository.save(messageMapper.convertToEntity(dto));
        log.info("DialogService in createMessage: message was create with id: " + message.getId());

        dialog.setLastMessage(message);
        dialogRepository.save(dialog);

        return messageMapper.convertToMessageShortDto(message);

    }

    private Long getUnreadMessageCountCurrentUser() {

        return messageRepository.countByRecipientIdAndReadStatus(
                getJwtUserIdFromSecurityContext(),
                ReadStatus.SENT);

    }

    private HashMap<Long, AccountDto> getAccountsDto(List<Dialog> dialogList) {

        HashMap<Long, AccountDto> accountDtoHashMap = new HashMap<>();
        Set<Long> accountsId = new HashSet<>();
        dialogList.forEach(d -> {
           accountsId.add(d.getUserId1());
           accountsId.add(d.getUserId2());
        });
        List<AccountDto> accountList = accountService.getAccountsByIds(accountsId);
        accountList.forEach(a -> {
            if(!accountDtoHashMap.containsKey(a.getId())) {
                accountDtoHashMap.put(a.getId(), a);
            }
        });
        return accountDtoHashMap;

    }

    private Dialog getDialog(Long authorId, Long companionId) {

        DialogSearchDto searchDto = new DialogSearchDto();
        searchDto.setIsDeleted(false);
        searchDto.setUserId(authorId);
        searchDto.setConversationPartnerId(companionId);

        List<Dialog> dialogList = dialogRepository.findAll(getDialogSpecification(searchDto));

        if (dialogList.size() == 0) {
            Dialog dialog = new Dialog();
            dialog.setIsDeleted(false);
            dialog.setUserId1(authorId);
            dialog.setUserId2(companionId);
            log.info("DialogService in getDialog: dialog was create with authorId: " + authorId + " and companionId " + companionId);

            return dialogRepository.save(dialog);
        }

        return dialogList.get(0);
    }

    private MessageSearchDto getMessageSearchDto(Long authorId, Long recipientId, ReadStatusDto status) {

        Dialog dialog = getDialog(authorId, recipientId);
        MessageSearchDto searchDto = new MessageSearchDto();
        searchDto.setDialogId(dialog.getId());
        searchDto.setIsDeleted(false);
        searchDto.setReadStatus(status);

        return searchDto;

    }

    private Specification<Message> getMessageSpecification(MessageSearchDto searchDto) {

        return getBaseSpecification(searchDto)
                .and(equal(Message_.dialogId, searchDto.getDialogId(), true))
                .and(equal(Message_.readStatus, messageMapper.convertReadStatusToEntity(searchDto.getReadStatus()), true));

    }

    private Specification<Dialog> getDialogSpecification(DialogSearchDto searchDto) {

        if (searchDto.getUserId() != null & searchDto.getConversationPartnerId() != null) {
            return getBaseSpecification(searchDto)
                    .and((equal(Dialog_.userId1, searchDto.getUserId(), true)
                    .and(equal(Dialog_.userId2, searchDto.getConversationPartnerId(), true)))
                    .or(equal(Dialog_.userId1, searchDto.getConversationPartnerId(), true)
                            .and(equal(Dialog_.userId2, searchDto.getUserId(), true))));
        } else {
            return getBaseSpecification(searchDto).and(equal(Dialog_.userId1, searchDto.getUserId(), true)
                    .or(equal(Dialog_.userId2, searchDto.getUserId(), true)));
        }

    }

    private Long getTimestamp() {

        return ZonedDateTime.now().toInstant().toEpochMilli();

    }

}
