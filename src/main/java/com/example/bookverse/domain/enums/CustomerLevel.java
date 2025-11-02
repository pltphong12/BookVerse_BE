package com.example.bookverse.domain.enums;

public enum CustomerLevel {
    BRONZE ("Đồng"),
    SILVER ("Bạc"),
    GOLD ("Vàng"),
    DIAMOND ("Kim cương");

    private final String displayName;

    CustomerLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
