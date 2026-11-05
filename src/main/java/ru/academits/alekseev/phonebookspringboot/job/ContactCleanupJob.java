package ru.academits.alekseev.phonebookspringboot.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;
import ru.academits.alekseev.phonebookspringboot.service.ContactsService;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class ContactCleanupJob {
    private final ContactsService contactsService;
    private static final Random random = new Random();

    public ContactCleanupJob(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @Scheduled(fixedRate = 10000)
    public void cleanup() {
        List<ContactDto> contacts = contactsService.getContacts(null);

        if (contacts.isEmpty()) {
            log.info("No contacts found");
            return;
        }

        ContactDto contact = contacts.get(random.nextInt(contacts.size()));
        contactsService.deleteContact(contact.getId());

        log.info("Deleted contact {}", contact.getId());
    }
}
