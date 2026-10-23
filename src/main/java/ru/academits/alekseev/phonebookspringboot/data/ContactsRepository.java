package ru.academits.alekseev.phonebookspringboot.data;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getContacts(String term);

    void createContact(Contact contact);

    boolean editContact(Contact contact);

    void deleteContact(int id);
}
