package ru.academits.alekseev.phonebookspringboot.controller;

import org.springframework.web.bind.annotation.*;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;
import ru.academits.alekseev.phonebookspringboot.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactsController {
    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping
    public List<ContactDto> getContacts(@RequestParam(required = false) String term) {
        return contactsService.getContacts(term);
    }

    @PostMapping
    public BaseResponse createContact(@RequestBody ContactDto contact) {
        return contactsService.createContact(contact);
    }

    @PutMapping
    public BaseResponse editContact(@RequestBody ContactDto contact) {
        return contactsService.editContact(contact);
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteContact(@PathVariable int id) {
        return contactsService.deleteContact(id);
    }
}
