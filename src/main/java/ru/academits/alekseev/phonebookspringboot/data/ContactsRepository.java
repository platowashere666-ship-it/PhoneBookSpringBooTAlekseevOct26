package ru.academits.alekseev.phonebookspringboot.data;

import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getContacts(String term);

    BaseResponse createContact(Contact contact);

    BaseResponse editContact(Contact contact);

    BaseResponse deleteContact(int id);

    BaseResponse phoneExistsValidation(String phone);
}
