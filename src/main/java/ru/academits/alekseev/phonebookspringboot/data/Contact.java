package ru.academits.alekseev.phonebookspringboot.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contact {
    Integer id;
    String surname;
    String name;
    String phone;
}
