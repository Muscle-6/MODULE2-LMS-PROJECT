package com.wanted.ailienlmsprogram.user.dto;

public enum MemberRank {

    REPTILIAN("파충류"),
    MINERVAL("미네르발"),
    NOVICE("초보자");

    private final String displayName;

    MemberRank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}