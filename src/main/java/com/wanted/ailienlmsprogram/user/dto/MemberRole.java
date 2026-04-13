package com.wanted.ailienlmsprogram.user.dto;


public enum MemberRole {

    STUDENT("학생"),
    INSTRUCTOR("강사"),
    ADMIN("관리자");

    private final String displayName;

    MemberRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}