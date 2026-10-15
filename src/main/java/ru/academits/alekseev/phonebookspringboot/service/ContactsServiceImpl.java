package ru.academits.alekseev.phonebookspringboot.service;

import org.springframework.stereotype.Service;
import ru.academits.alekseev.phonebookspringboot.converter.ContactsConverter;
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

        return contactsRepository.createContact(contactsConverter.convertToModel(contact));
    }

    @Override
    public BaseResponse editContact(ContactDto contact) {
        BaseResponse validationResult = validateContact(contact);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        return contactsRepository.editContact(contactsConverter.convertToModel(contact));
    }

    @Override
    public BaseResponse deleteContact(int id) {
        return contactsRepository.deleteContact(id);
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

        BaseResponse phoneExistsValidationResult = contactsRepository.phoneExistsValidation(contact.getPhone());

        if (!phoneExistsValidationResult.isSuccess()) {
            return phoneExistsValidationResult;
        }

        return BaseResponse.success();
    }
}
