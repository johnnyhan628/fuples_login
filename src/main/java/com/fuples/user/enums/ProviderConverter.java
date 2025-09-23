package com.fuples.user.enums;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProviderConverter implements AttributeConverter<Provider, String> {
    @Override
    public String convertToDatabaseColumn(Provider status) {
        return status.toDbValue();
    }

    @Override
    public Provider convertToEntityAttribute(String dbValue) {
        return Provider.fromDbValue(dbValue);
    }
}
