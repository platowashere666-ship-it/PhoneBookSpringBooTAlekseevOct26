package ru.academits.alekseev.phonebookspringboot.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.academits.alekseev.phonebookspringboot.service.ContactsService;

@Slf4j
@Component
public class ContactCleanupJob {
    private final ContactsService contactsService;

    public ContactCleanupJob(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @Scheduled(fixedRate = 10000)
    public void cleanup() {
        contactsService.deleteRandomContact();
    }
}
