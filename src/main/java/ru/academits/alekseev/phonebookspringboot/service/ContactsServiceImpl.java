package ru.academits.alekseev.phonebookspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.academits.alekseev.phonebookspringboot.converter.ContactsConverter;
import ru.academits.alekseev.phonebookspringboot.data.Contact;
import ru.academits.alekseev.phonebookspringboot.data.ContactsRepository;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;
import ru.academits.alekseev.phonebookspringboot.job.ContactCleanupJob;

import java.util.List;

@Slf4j
@Service
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;
    private final ContactsConverter contactsConverter;
    private final ContactCleanupJob contactCleanupJob;

    public ContactsServiceImpl(ContactsRepository contactsRepository, ContactsConverter contactsConverter, ContactCleanupJob contactCleanupJob) {
        this.contactsRepository = contactsRepository;
        this.contactsConverter = contactsConverter;
        this.contactCleanupJob = contactCleanupJob;
    }

    @Override
    public List<ContactDto> getContacts(String term) {
        log.debug("getContacts({})", term);

        return contactsConverter.convertToDto(contactsRepository.getContacts(term));
    }

    @Override
    public BaseResponse createContact(ContactDto contact) {
        BaseResponse validationResult = validateContact(contact);

        if (!validationResult.isSuccess()) {
            log.error("createContact({})", validationResult);

            return validationResult;
        }

        contactsRepository.createContact(contactsConverter.convertToModel(contact));
        log.debug("createContact({})", contact);

        return BaseResponse.success();
    }

    @Override
    public BaseResponse editContact(ContactDto contact) {
        BaseResponse validationResult = validateContact(contact);

        if (!validationResult.isSuccess()) {
            log.error("editContact({})", validationResult);

            return validationResult;
        }

        if (!contactsRepository.editContact(contactsConverter.convertToModel(contact))) {
            log.error("editContact(Contact not found): {})", contact);

            return BaseResponse.error("Contact not found");
        }

        log.debug("editContact({})", contact);

        return BaseResponse.success();
    }

    @Override
    public BaseResponse deleteContact(int id) {
        contactsRepository.deleteContact(id);
        log.debug("deleteContact({})", id);

        return BaseResponse.success();
    }

    private BaseResponse validateContact(ContactDto contact) {
        if (contact == null) {
            return BaseResponse.error("Contact is null");
        }

        if (contact.getSurname() == null || contact.getSurname().isBlank()) {
            return BaseResponse.error("Surname is empty or null");
        }

        if (contact.getName() == null || contact.getName().isBlank()) {
            return BaseResponse.error("Name is empty or null");
        }

        if (contact.getPhone() == null || contact.getPhone().isBlank()) {
            return BaseResponse.error("Phone is empty or null");
        }

        BaseResponse phoneValidationResult = validatePhone(contact.getPhone());

        if (!phoneValidationResult.isSuccess()) {
            return phoneValidationResult;
        }

        return BaseResponse.success();
    }

    private BaseResponse validatePhone(String phone) {
        List<Contact> contacts = contactsRepository.getContacts(null);

        boolean phoneExists = contacts.stream()
                .anyMatch(c -> c.getPhone().equals(phone));

        if (phoneExists) {
            return BaseResponse.error("Phone already exists");
        }

        return BaseResponse.success();
    }

    @Scheduled(fixedRate = 10000)
    public void cleanup() {
        List<Contact> contacts = contactsRepository.getContacts(null);

        if (contacts.isEmpty()) {
            log.info("No contacts found");
            return;
        }

        contactCleanupJob.cleanup(contacts);
    }
}
