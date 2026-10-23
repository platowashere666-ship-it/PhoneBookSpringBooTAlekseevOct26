package ru.academits.alekseev.phonebookspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.alekseev.phonebookspringboot.data.Contact;
import ru.academits.alekseev.phonebookspringboot.data.ContactsRepository;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class ContactCleanupJob {
    private final ContactsRepository contactsRepository;
    private final Random random = new Random();

    public ContactCleanupJob(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void cleanup() {
        List<Contact> contacts = contactsRepository.getContacts(null);

        if (contacts.isEmpty()) {
            log.info("No contacts found");
            return;
        }

        Contact contact = contacts.get(random.nextInt(contacts.size()));

        contactsRepository.deleteContact(contact.getId());

        log.info("Deleted contact {}", contact.getId());
    }
}
