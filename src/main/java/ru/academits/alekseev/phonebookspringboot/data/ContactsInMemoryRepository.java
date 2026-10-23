package ru.academits.alekseev.phonebookspringboot.data;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ContactsInMemoryRepository implements ContactsRepository {
    private final List<Contact> contacts = new ArrayList<>();
    private int newContactId = 1;

    @Override
    public List<Contact> getContacts(String term) {
        String upperCaseTerm = term != null ?
                term.trim().toUpperCase()
                : null;

        synchronized (contacts) {
            return contacts.stream()
                    .filter(c -> upperCaseTerm == null
                            || c.getSurname().toUpperCase().contains(upperCaseTerm)
                            || c.getName().toUpperCase().contains(upperCaseTerm)
                            || c.getPhone().toUpperCase().contains(upperCaseTerm))
                    .map(c -> new Contact(c.getId(), c.getSurname(), c.getName(), c.getPhone()))
                    .toList();
        }
    }

    @Override
    public void createContact(Contact contact) {
        synchronized (contacts) {
            contacts.add(new Contact(newContactId, contact.getSurname(), contact.getName(), contact.getPhone()));
            ++newContactId;
        }
    }

    @Override
    public boolean editContact(Contact contact) {
        synchronized (contacts) {
            Contact repositoryContact = contacts.stream()
                    .filter(c -> Objects.equals(c.getId(), contact.getId()))
                    .findFirst()
                    .orElse(null);

            if (repositoryContact == null) {
                return false;
            }

            repositoryContact.setSurname(contact.getSurname());
            repositoryContact.setName(contact.getName());
            repositoryContact.setPhone(contact.getPhone());

            return true;
        }
    }

    @Override
    public void deleteContact(int id) {
        synchronized (contacts) {
            contacts.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .ifPresent(contacts::remove);
        }
    }
}
