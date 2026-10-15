package ru.academits.alekseev.phonebookspringboot.converter;

import java.util.List;

public interface Converter<M, D> {
    D convertToDto(M model);

    M convertToModel(D dto);

    default List<D> convertToDto(List<M> list) {
        return list.stream().map(this::convertToDto).toList();
    }

    default List<M> convertToModel(List<D> list) {
        return list.stream().map(this::convertToModel).toList();
    }
}
