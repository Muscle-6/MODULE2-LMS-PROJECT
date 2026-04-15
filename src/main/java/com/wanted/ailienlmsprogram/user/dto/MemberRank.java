package com.wanted.ailienlmsprogram.user.dto;

public enum MemberRank {

    REPTILIAN("랩틸리언"),
    MINERVAL("미네르발"),
    NOVICE("노비스");

    private final String displayName;

    MemberRank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}