package com.wanted.ailienlmsprogram.coursecommand.entity;

public enum CourseStatus {

    PUBLISHED("공개"),
    DRAFT("비공개");

    private final String displayName;

    CourseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}