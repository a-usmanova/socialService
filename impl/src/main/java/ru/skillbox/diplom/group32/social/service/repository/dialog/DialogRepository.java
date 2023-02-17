package ru.skillbox.diplom.group32.social.service.repository.dialog;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.dialog.Dialog;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;


@Repository
public interface DialogRepository extends BaseRepository<Dialog>, PagingAndSortingRepository<Dialog, Long> {
}
