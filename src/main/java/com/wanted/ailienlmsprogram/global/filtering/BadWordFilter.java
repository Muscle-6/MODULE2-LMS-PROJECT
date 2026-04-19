package com.wanted.ailienlmsprogram.global.filtering;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//실제 필터로직
@Component
@RequiredArgsConstructor
public class BadWordFilter {

    private final BadWordLoader badWordLoader;

    public void validate(String fieldName, String value) {
        if (!StringUtils.hasText(value)) {
            return;
        }

        String normalized = BadWordNormalizer.normalizeInput(value);
        String compact = BadWordNormalizer.compactInput(value);

        for (String badWord : badWordLoader.getBadWords()) {
            if (normalized.contains(badWord) || compact.contains(badWord)) {
                throw new BadWordDetectedException(
                        buildMessage(fieldName)
                );
            }
        }
    }

    private String buildMessage(String fieldName) {
        return switch (fieldName) {
            case "postTitle" -> "게시글 제목에 비속어가 포함되어 등록할 수 없습니다.";
            case "postContent" -> "게시글 내용에 비속어가 포함되어 등록할 수 없습니다.";
            default -> "비속어가 포함된 내용은 등록할 수 없습니다.";
        };
    }
}