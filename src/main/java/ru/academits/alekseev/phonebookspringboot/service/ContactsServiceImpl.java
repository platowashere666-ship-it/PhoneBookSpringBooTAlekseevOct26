package ru.academits.alekseev.phonebookspringboot.service;

import org.springframework.stereotype.Service;
import ru.academits.alekseev.phonebookspringboot.converter.ContactsConverter;
import ru.academits.alekseev.phonebookspringboot.data.Contact;
import ru.academits.alekseev.phonebookspringboot.data.ContactsRepository;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;

import java.util.List;

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
        return contactsConverter.convertToDto(contactsRepository.getContacts(term));
    }

    @Override
    public BaseResponse createContact(ContactDto contact) {
        BaseResponse validationResult = validateContact(contact);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        contactsRepository.createContact(contactsConverter.convertToModel(contact));

        return BaseResponse.success();
    }

    @Override
    public BaseResponse editContact(ContactDto contact) {
        try {
            BaseResponse validationResult = validateContact(contact);

            if (!validationResult.isSuccess()) {
                return validationResult;
            }

            if (!contactsRepository.editContact(contactsConverter.convertToModel(contact))) {
                throw new IllegalAccessException("Contact not found");
            }

            return BaseResponse.success();
        } catch (IllegalAccessException e) {
            return BaseResponse.error(e.getMessage());
        }
    }

    @Override
    public BaseResponse deleteContact(int id) {
        contactsRepository.deleteContact(id);

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
}
