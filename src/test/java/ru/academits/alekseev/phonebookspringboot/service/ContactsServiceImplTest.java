package ru.academits.alekseev.phonebookspringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.academits.alekseev.phonebookspringboot.converter.ContactsConverter;
import ru.academits.alekseev.phonebookspringboot.data.Contact;
import ru.academits.alekseev.phonebookspringboot.data.ContactsRepository;
import ru.academits.alekseev.phonebookspringboot.dto.BaseResponse;
import ru.academits.alekseev.phonebookspringboot.dto.ContactDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactsServiceImplTest {
    @Mock
    ContactsRepository contactsRepository;

    @Mock
    ContactsConverter contactsConverter;

    @InjectMocks
    ContactsServiceImpl contactsService;

    private ContactDto contactDto;
    private Contact contactModel;

    @BeforeEach
    public void setUp() {
        contactDto = new ContactDto(1, "Snake", "Lizard", "859445495");
        contactModel = new Contact(1, "Snake", "Lizard", "859445495");
    }

    @Test
    void getContacts_ValidTerm_ReturnsContacts() {
        when(contactsRepository.getContacts("SnAKE")).thenReturn(List.of(contactModel));
        when(contactsConverter.convertToDto(List.of(contactModel))).thenReturn(List.of(contactDto));

        List<ContactDto> result = contactsService.getContacts("SnAKE");

        assertThat(result).containsExactly(contactDto);
        verify(contactsRepository, times(1)).getContacts("SnAKE");
    }

    @Test
    void createContact_ValidContact_ReturnsSuccess() {
        when(contactsRepository.getContacts(null)).thenReturn(List.of());
        when(contactsConverter.convertToModel(contactDto)).thenReturn(contactModel);

        BaseResponse response = contactsService.createContact(contactDto);

        assertThat(response.isSuccess()).isTrue();
        verify(contactsRepository, times(1)).createContact(contactModel);
    }

    @Test
    void createContact_InvalidContact_ReturnsError() {
        contactDto.setPhone(null);

        BaseResponse response = contactsService.createContact(contactDto);

        assertThat(response.isSuccess()).isFalse();
        verifyNoInteractions(contactsRepository);
    }

    @Test
    void editContact_ValidContact_ReturnsSuccess() {
        when(contactsRepository.getContacts(null)).thenReturn(List.of());
        when(contactsConverter.convertToModel(contactDto)).thenReturn(contactModel);
        when(contactsRepository.editContact(contactModel)).thenReturn(true);

        BaseResponse response = contactsService.editContact(contactDto);

        assertThat(response.isSuccess()).isTrue();
        verify(contactsRepository, times(1)).editContact(contactModel);
    }

    @Test
    void deleteContact_ReturnsSuccess() {
        BaseResponse response = contactsService.deleteContact(1);

        assertThat(response.isSuccess()).isTrue();
        verify(contactsRepository, times(1)).deleteContact(1);
    }
}
