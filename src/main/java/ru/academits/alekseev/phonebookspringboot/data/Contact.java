package ru.academits.alekseev.phonebookspringboot.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contact {
    private Integer id;
    private String surname;
    private String name;
    private String phone;
}
