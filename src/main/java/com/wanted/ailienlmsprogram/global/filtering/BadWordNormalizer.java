package com.wanted.ailienlmsprogram.global.filtering;

import java.util.Locale;

//전처리 유틸 코드
public final class BadWordNormalizer {

    private BadWordNormalizer() {
    }

    //소문자화 + trim
    public static String normalizeDictionaryToken(String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.ROOT).trim();
    }

    //연속 공백 제거
    public static String normalizeInput(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase(Locale.ROOT)
                .replaceAll("\\p{Cntrl}", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    //공백 제거 + 특수문자 제거
    public static String compactInput(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "")
                .replaceAll("[^0-9a-z가-힣ㄱ-ㅎㅏ-ㅣ]", "");
    }
}