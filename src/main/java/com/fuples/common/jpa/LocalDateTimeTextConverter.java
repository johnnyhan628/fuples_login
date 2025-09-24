package com.fuples.common.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class LocalDateTimeTextConverter implements AttributeConverter<LocalDateTime, String> {

    // SQLite가 잘 읽는 포맷 (공백 구분, 밀리초 포함)
    public static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute == null ? null : attribute.format(F);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isBlank() ? null : LocalDateTime.parse(dbData, F);
    }
}