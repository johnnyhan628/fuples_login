package com.fuples.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Provider {
    LOCAL("로컬"),
    KAKAO("카카오");

    private final String description;

    public String toDbValue() {
        return this.name().toLowerCase();
    }

    public static Provider fromDbValue(String dbValue) {
        return Provider.valueOf(dbValue.toUpperCase());
    }
}