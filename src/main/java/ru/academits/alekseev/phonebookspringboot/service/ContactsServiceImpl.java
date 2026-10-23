package ru.academits.alekseev.phonebookspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.academits.alekseev.phonebookspringboot.converter.ContactsConverter;
import ru.academits.alekseev.phonebookspringboot.data.Contact;
import ru.academits.alekseev.phonebookspringboot.data.ContactsRepository;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;

import java.util.List;

@Slf4j
@Service
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;
    private final ContactsConverter contactsConverter;

    public ContactsServiceImpl(ContactsRepository contactsRepository, ContactsConverter contactsConverter) {
        this.contactsRepository = contactsRepository;
        this.contactsConverter = contactsConverter;
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
        try {
            BaseResponse validationResult = validateContact(contact);

            if (!validationResult.isSuccess()) {
                log.error("editContact({})", validationResult);

                return validationResult;
            }

            if (!contactsRepository.editContact(contactsConverter.convertToModel(contact))) {
                throw new IllegalAccessException("Contact not found");
            }

            log.debug("editContact({})", contact);

            return BaseResponse.success();
        } catch (IllegalAccessException e) {
            log.error("editContact({})", contact, e);

            return BaseResponse.error(e.getMessage());
        }
    }

    @Override
    public BaseResponse deleteContact(int id) {
        contactsRepository.deleteContact(id);
        log.debug("deleteContact({})", id);

        return BaseResponse.success();
    }

    private BaseResponse validateContact(ContactDto contact) {
        if (contact == null) {
            log.error("validateContact(null)");

            return BaseResponse.error("Contact is null");
        }

        if (contact.getSurname() == null || contact.getSurname().isBlank()) {
            log.error("validateContact({})", contact);

            return BaseResponse.error("Surname is empty or null");
        }

        if (contact.getName() == null || contact.getName().isBlank()) {
            log.error("validateContact({})", contact);

            return BaseResponse.error("Name is empty or null");
        }

        if (contact.getPhone() == null || contact.getPhone().isBlank()) {
            log.error("validateContact({})", contact);

            return BaseResponse.error("Phone is empty or null");
        }

        BaseResponse phoneValidationResult = validatePhone(contact.getPhone());

        if (!phoneValidationResult.isSuccess()) {
            log.error("validateContact({})", phoneValidationResult);

            return phoneValidationResult;
        }

        log.debug("validateContact({})", contact);

        return BaseResponse.success();
    }

    private BaseResponse validatePhone(String phone) {
        List<Contact> contacts = contactsRepository.getContacts(null);

        boolean phoneExists = contacts.stream()
                .anyMatch(c -> c.getPhone().equals(phone));

        if (phoneExists) {
            log.error("validatePhone({})", phone);

            return BaseResponse.error("Phone already exists");
        }

        log.debug("validatePhone({})", phone);

        return BaseResponse.success();
    }
}
