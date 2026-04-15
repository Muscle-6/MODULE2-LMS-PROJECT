package com.wanted.ailienlmsprogram.user.dto;

public enum AccountStatus {

    ACTIVE("활성"),
    INACTIVE("비활성"),
    BANNED("정지");

    private final String displayName;

    AccountStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}