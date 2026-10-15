package ru.academits.alekseev.phonebookspringboot.service;

import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;

import java.util.List;

public interface ContactsService {
    List<ContactDto> getContacts(String term);

    BaseResponse createContact(ContactDto contact);

    BaseResponse editContact(ContactDto contact);

    BaseResponse deleteContact(int id);
}
