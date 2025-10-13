package com.example.bookverse.domain.enums;

public enum CoverFormat {
    PAPERBACK("Bìa mềm"),
    HARDCOVER("Bìa cứng");

    private final String displayName;

    CoverFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}