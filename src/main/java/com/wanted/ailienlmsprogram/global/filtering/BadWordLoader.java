package com.wanted.ailienlmsprogram.global.filtering;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

/*원본 텍스트 파일 기준 전처리 로직
*
* 1. 원본 txt 파일이 한줄 콤마 구분이어도 읽힘
* 2. 미리 줄바꿈을 처리해도 읽힘
* 3. whitelist로 2차 방어 가능
*
* 즉 메모장에서 수정한 내용이 팀원들이 다시 원본 txt 파일을 올려도
* 동일한 로직으로 전처리가 가능하도록 구현한 클래스.
* 평소에는 사용이 되지 않는다.*/
@Component
@Getter
public class BadWordLoader {

    private static final Set<String> DEFAULT_WHITELIST = Set.of(
            "공지", "공지사항", "운영자", "마스터"
    );

    private final Set<String> badWords = new LinkedHashSet<>();

    @PostConstruct
    public void load() throws Exception {
        ClassPathResource resource = new ClassPathResource("filtering/badwords.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[,\\r\\n]+");

                for (String token : tokens) {
                    String normalized = BadWordNormalizer.normalizeDictionaryToken(token);

                    if (normalized.isBlank()) {
                        continue;
                    }

                    if (DEFAULT_WHITELIST.contains(normalized)) {
                        continue;
                    }

                    badWords.add(normalized);
                }
            }
        }
    }
}