package ru.academits.alekseev.phonebookspringboot.data;

import org.springframework.stereotype.Repository;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;

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
    public BaseResponse createContact(Contact contact) {
        synchronized (contacts) {
            BaseResponse phoneExistsValidationResult = phoneExistsValidation(contact.getPhone());

            if (!phoneExistsValidationResult.isSuccess()) {
                return phoneExistsValidationResult;
            }

            contacts.add(new Contact(newContactId, contact.getSurname(), contact.getName(), contact.getPhone()));
            ++newContactId;
        }

        return BaseResponse.success();
    }

    @Override
    public BaseResponse editContact(Contact contact) {
        synchronized (contacts) {
            Contact repositoryContact = contacts.stream()
                    .filter(c -> Objects.equals(c.getId(), contact.getId()))
                    .findFirst()
                    .orElse(null);

            if (repositoryContact == null) {
                return BaseResponse.error("Contact not found");
            }

            BaseResponse phoneExistsValidationResult = phoneExistsValidation(contact.getPhone());

            if (!phoneExistsValidationResult.isSuccess()) {
                return phoneExistsValidationResult;
            }

            repositoryContact.setSurname(contact.getSurname());
            repositoryContact.setName(contact.getName());
            repositoryContact.setPhone(contact.getPhone());
        }

        return BaseResponse.success();
    }

    @Override
    public BaseResponse deleteContact(int id) {
        synchronized (contacts) {
            contacts.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .ifPresent(contacts::remove);
        }

        return BaseResponse.success();
    }

    @Override
    public BaseResponse phoneExistsValidation(String phone) {
        synchronized (contacts) {
            boolean phoneExists = contacts.stream()
                    .anyMatch(c -> c.getPhone().equals(phone));

            if (phoneExists) {
                return BaseResponse.error("Phone already exists");
            }

            return BaseResponse.success();
        }
    }
}
