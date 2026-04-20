USE module02_lms;
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 기존 데이터 초기화 (FK 순서: 자식 → 부모)
-- =============================================
TRUNCATE TABLE REFUND;
TRUNCATE TABLE PAYMENT_ITEM;
TRUNCATE TABLE PAYMENT;
TRUNCATE TABLE CART;
TRUNCATE TABLE REPORT;
TRUNCATE TABLE LECTURE_PROGRESS;
TRUNCATE TABLE QNA;
TRUNCATE TABLE ENROLLMENT;
TRUNCATE TABLE COURSE_NOTICE;
TRUNCATE TABLE CONTINENT_COMMENT;
TRUNCATE TABLE CONTINENT_POST;
TRUNCATE TABLE LECTURE;
TRUNCATE TABLE COURSE;
TRUNCATE TABLE CONTINENT;
TRUNCATE TABLE MEMBER;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- MEMBER (강사 10명: ID 1~10, 학생 20명: ID 11~30)
-- 비밀번호: 1234
-- =============================================
INSERT INTO MEMBER (login_id, email, password, member_name, phone, member_role, member_status, member_rank, created_at, updated_at) VALUES
('instructor01', 'instructor01@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사김우주', '010-1111-0001', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor02', 'instructor02@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사이외계', '010-1111-0002', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor03', 'instructor03@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사박행성', '010-1111-0003', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor04', 'instructor04@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사최은하', '010-1111-0004', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor05', 'instructor05@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사정성운', '010-1111-0005', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor06', 'instructor06@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사한혜성', '010-1111-0006', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor07', 'instructor07@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사오성층', '010-1111-0007', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor08', 'instructor08@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사서대기', '010-1111-0008', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor09', 'instructor09@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사남궤도', '010-1111-0009', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('instructor10', 'instructor10@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '강사류소행', '010-1111-0010', 'INSTRUCTOR', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('student01', 'student01@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생001', '010-2222-0001', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student02', 'student02@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생002', '010-2222-0002', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student03', 'student03@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생003', '010-2222-0003', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student04', 'student04@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생004', '010-2222-0004', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student05', 'student05@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생005', '010-2222-0005', 'STUDENT', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('student06', 'student06@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생006', '010-2222-0006', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student07', 'student07@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생007', '010-2222-0007', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student08', 'student08@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생008', '010-2222-0008', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student09', 'student09@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생009', '010-2222-0009', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student10', 'student10@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생010', '010-2222-0010', 'STUDENT', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('student11', 'student11@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생011', '010-2222-0011', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student12', 'student12@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생012', '010-2222-0012', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student13', 'student13@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생013', '010-2222-0013', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student14', 'student14@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생014', '010-2222-0014', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student15', 'student15@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생015', '010-2222-0015', 'STUDENT', 'ACTIVE', 'REPTILIAN', NOW(), NOW()),
('student16', 'student16@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생016', '010-2222-0016', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student17', 'student17@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생017', '010-2222-0017', 'STUDENT', 'ACTIVE', 'NOVICE', NOW(), NOW()),
('student18', 'student18@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생018', '010-2222-0018', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student19', 'student19@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생019', '010-2222-0019', 'STUDENT', 'ACTIVE', 'MINERVAL', NOW(), NOW()),
('student20', 'student20@test.com', '$2a$10$Q4O6rm5aFLDT4VzpxHRu8uQZD4aEU3FTeQftGqJQnftcUpIWfTnSm', '학생020', '010-2222-0020', 'STUDENT', 'ACTIVE', 'REPTILIAN', NOW(), NOW());

-- =============================================
-- CONTINENT (10개)
-- =============================================
INSERT INTO CONTINENT (continent_name, continent_description) VALUES
('알파 센타우리', '가장 가까운 항성계 기반 강좌 모음'),
('오리온 성운', '성운 탐험 및 우주 항법 강좌'),
('안드로메다', '은하 간 이동 기술 강좌'),
('마젤란 성운', '위성 은하 탐사 전문 강좌'),
('시리우스', '밝은 별 기반 에너지 강좌'),
('베텔게우스', '초거성 생존 전략 강좌'),
('플레이아데스', '성단 항법 기술 강좌'),
('카시오페이아', '항성 좌표 탐색 강좌'),
('용골자리', '거대 성운 탐험 강좌'),
('삼각형자리', '삼각 은하 기반 과학 강좌');

-- =============================================
-- COURSE (강사당 5개씩, 총 50개)
-- instructor01(ID=1) ~ instructor10(ID=10)
-- =============================================
INSERT INTO COURSE (continent_id, instructor_id, course_title, course_description, course_price, course_status, created_at) VALUES
-- instructor01 (5개)
(1, 1, '우주 항법 기초 A', '알파 센타우리 항법 기초 강좌입니다.', 50000, 'PUBLISHED', NOW()),
(2, 1, '우주 항법 기초 B', '오리온 성운 항법 기초 강좌입니다.', 55000, 'PUBLISHED', NOW()),
(3, 1, '우주 항법 기초 C', '안드로메다 항법 기초 강좌입니다.', 60000, 'PUBLISHED', NOW()),
(4, 1, '우주 항법 기초 D', '마젤란 항법 기초 강좌입니다.', 65000, 'PUBLISHED', NOW()),
(5, 1, '우주 항법 기초 E', '시리우스 항법 기초 강좌입니다.', 70000, 'PUBLISHED', NOW()),
-- instructor02 (5개)
(1, 2, '성운 생존 전략 A', '알파 센타우리 생존 전략입니다.', 50000, 'PUBLISHED', NOW()),
(2, 2, '성운 생존 전략 B', '오리온 성운 생존 전략입니다.', 55000, 'PUBLISHED', NOW()),
(3, 2, '성운 생존 전략 C', '안드로메다 생존 전략입니다.', 60000, 'PUBLISHED', NOW()),
(4, 2, '성운 생존 전략 D', '마젤란 생존 전략입니다.', 65000, 'PUBLISHED', NOW()),
(5, 2, '성운 생존 전략 E', '시리우스 생존 전략입니다.', 70000, 'PUBLISHED', NOW()),
-- instructor03 (5개)
(6, 3, '은하 이동술 A', '베텔게우스 이동술 강좌입니다.', 80000, 'PUBLISHED', NOW()),
(7, 3, '은하 이동술 B', '플레이아데스 이동술 강좌입니다.', 85000, 'PUBLISHED', NOW()),
(8, 3, '은하 이동술 C', '카시오페이아 이동술 강좌입니다.', 90000, 'PUBLISHED', NOW()),
(9, 3, '은하 이동술 D', '용골자리 이동술 강좌입니다.', 95000, 'PUBLISHED', NOW()),
(10, 3, '은하 이동술 E', '삼각형자리 이동술 강좌입니다.', 100000, 'PUBLISHED', NOW()),
-- instructor04 (5개)
(1, 4, '에너지학 기초 A', '알파 센타우리 에너지학입니다.', 45000, 'PUBLISHED', NOW()),
(2, 4, '에너지학 기초 B', '오리온 에너지학입니다.', 50000, 'PUBLISHED', NOW()),
(3, 4, '에너지학 기초 C', '안드로메다 에너지학입니다.', 55000, 'PUBLISHED', NOW()),
(4, 4, '에너지학 기초 D', '마젤란 에너지학입니다.', 60000, 'PUBLISHED', NOW()),
(5, 4, '에너지학 기초 E', '시리우스 에너지학입니다.', 65000, 'PUBLISHED', NOW()),
-- instructor05 (5개)
(6, 5, '초거성 탐험 A', '베텔게우스 탐험 강좌입니다.', 75000, 'PUBLISHED', NOW()),
(7, 5, '초거성 탐험 B', '플레이아데스 탐험 강좌입니다.', 80000, 'PUBLISHED', NOW()),
(8, 5, '초거성 탐험 C', '카시오페이아 탐험 강좌입니다.', 85000, 'PUBLISHED', NOW()),
(9, 5, '초거성 탐험 D', '용골자리 탐험 강좌입니다.', 90000, 'PUBLISHED', NOW()),
(10, 5, '초거성 탐험 E', '삼각형자리 탐험 강좌입니다.', 95000, 'PUBLISHED', NOW()),
-- instructor06 (5개)
(1, 6, '항성 좌표 A', '알파 센타우리 좌표 강좌입니다.', 55000, 'PUBLISHED', NOW()),
(2, 6, '항성 좌표 B', '오리온 좌표 강좌입니다.', 60000, 'PUBLISHED', NOW()),
(3, 6, '항성 좌표 C', '안드로메다 좌표 강좌입니다.', 65000, 'PUBLISHED', NOW()),
(4, 6, '항성 좌표 D', '마젤란 좌표 강좌입니다.', 70000, 'PUBLISHED', NOW()),
(5, 6, '항성 좌표 E', '시리우스 좌표 강좌입니다.', 75000, 'PUBLISHED', NOW()),
-- instructor07 (5개)
(6, 7, '성단 항법 A', '베텔게우스 항법 강좌입니다.', 60000, 'PUBLISHED', NOW()),
(7, 7, '성단 항법 B', '플레이아데스 항법 강좌입니다.', 65000, 'PUBLISHED', NOW()),
(8, 7, '성단 항법 C', '카시오페이아 항법 강좌입니다.', 70000, 'PUBLISHED', NOW()),
(9, 7, '성단 항법 D', '용골자리 항법 강좌입니다.', 75000, 'PUBLISHED', NOW()),
(10, 7, '성단 항법 E', '삼각형자리 항법 강좌입니다.', 80000, 'PUBLISHED', NOW()),
-- instructor08 (5개)
(1, 8, '은하 과학 A', '알파 센타우리 과학 강좌입니다.', 85000, 'PUBLISHED', NOW()),
(2, 8, '은하 과학 B', '오리온 과학 강좌입니다.', 90000, 'PUBLISHED', NOW()),
(3, 8, '은하 과학 C', '안드로메다 과학 강좌입니다.', 95000, 'PUBLISHED', NOW()),
(4, 8, '은하 과학 D', '마젤란 과학 강좌입니다.', 100000, 'PUBLISHED', NOW()),
(5, 8, '은하 과학 E', '시리우스 과학 강좌입니다.', 50000, 'PUBLISHED', NOW()),
-- instructor09 (5개)
(6, 9, '우주 물리 A', '베텔게우스 물리 강좌입니다.', 70000, 'PUBLISHED', NOW()),
(7, 9, '우주 물리 B', '플레이아데스 물리 강좌입니다.', 75000, 'PUBLISHED', NOW()),
(8, 9, '우주 물리 C', '카시오페이아 물리 강좌입니다.', 80000, 'PUBLISHED', NOW()),
(9, 9, '우주 물리 D', '용골자리 물리 강좌입니다.', 85000, 'PUBLISHED', NOW()),
(10, 9, '우주 물리 E', '삼각형자리 물리 강좌입니다.', 90000, 'PUBLISHED', NOW()),
-- instructor10 (5개)
(1, 10, '항성 진화 A', '알파 센타우리 진화 강좌입니다.', 55000, 'PUBLISHED', NOW()),
(2, 10, '항성 진화 B', '오리온 진화 강좌입니다.', 60000, 'PUBLISHED', NOW()),
(3, 10, '항성 진화 C', '안드로메다 진화 강좌입니다.', 65000, 'PUBLISHED', NOW()),
(4, 10, '항성 진화 D', '마젤란 진화 강좌입니다.', 70000, 'PUBLISHED', NOW()),
(5, 10, '항성 진화 E', '시리우스 진화 강좌입니다.', 75000, 'PUBLISHED', NOW());

-- =============================================
-- LECTURE (강좌당 10개씩, 총 500개)
-- 공개 샘플 영상 URL 사용
-- =============================================
-- course 1~10 (강좌 1~10 각 10강)
INSERT INTO LECTURE (course_id, lecture_title, lecture_description, lecture_order_index, video_url, created_at)
SELECT
    c.course_id,
    CONCAT(c.course_title, ' ', n.n, '강'),
    CONCAT(n.n, '번째 강의입니다.'),
    n.n,
    ELT(1 + (n.n % 9),
        'https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4',
        'https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4'
    ),
    NOW()
FROM COURSE c
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
) n;

-- =============================================
-- COURSE_NOTICE (강좌당 5개씩, 총 250개)
-- =============================================
INSERT INTO COURSE_NOTICE (course_id, author_id, notice_title, notice_content, created_at, updated_at)
SELECT
    c.course_id,
    c.instructor_id,
    CONCAT(c.course_title, ' 공지사항 ', n.n),
    CONCAT(n.n, '번째 공지입니다. 수강생 여러분 확인 부탁드립니다.'),
    NOW(),
    NOW()
FROM COURSE c
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) n;

-- =============================================
-- ENROLLMENT (학생당 10개씩, 총 200개)
-- 학생 ID 11~30, 강좌 ID 1~50 순환
-- =============================================
INSERT INTO ENROLLMENT (member_id, course_id, enrollment_status, created_at, enrollment_progress_rate)
SELECT
    m.member_id,
    ((m.member_id - 11) * 10 + n.n) AS course_id,
    'ACTIVE',
    NOW(),
    ROUND(RAND() * 100, 2)
FROM MEMBER m
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
) n
WHERE m.member_role = 'STUDENT'
  AND ((m.member_id - 11) * 10 + n.n) BETWEEN 1 AND 50;

-- =============================================
-- LECTURE_PROGRESS (수강생별 첫 5강 완료, 총 약 500개)
-- =============================================
INSERT INTO LECTURE_PROGRESS (member_id, lecture_id, progress_is_completed)
SELECT DISTINCT
    e.member_id,
    l.lecture_id,
    TRUE
FROM ENROLLMENT e
JOIN LECTURE l ON l.course_id = e.course_id
WHERE l.lecture_order_index <= 5;

-- =============================================
-- QNA (강좌당 10개씩, 총 500개)
-- Entity 기준 course_id FK 사용
-- =============================================
INSERT INTO QNA (course_id, author_id, qna_title, qna_content, qna_is_deleted, created_at)
SELECT
    c.course_id,
    11 + (c.course_id % 20),
    CONCAT(c.course_title, ' 질문 ', n.n),
    CONCAT(c.course_title, '에 대한 ', n.n, '번째 질문입니다.'),
    FALSE,
    NOW()
FROM COURSE c
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
) n;
