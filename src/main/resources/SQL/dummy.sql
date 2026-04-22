USE module02_lms;
SET FOREIGN_KEY_CHECKS = 0;


TRUNCATE TABLE `REFUND`;
TRUNCATE TABLE `REPORT`;
TRUNCATE TABLE `LECTURE_PROGRESS`;
TRUNCATE TABLE `QNA`;
TRUNCATE TABLE `ENROLLMENT`;
TRUNCATE TABLE `CART`;
TRUNCATE TABLE `PAYMENT_ITEM`;
TRUNCATE TABLE `PAYMENT`;
TRUNCATE TABLE `CONTINENT_COMMENT`;
TRUNCATE TABLE `CONTINENT_POST`;
TRUNCATE TABLE `COURSE_NOTICE`;
TRUNCATE TABLE `LECTURE`;
TRUNCATE TABLE `COURSE`;
TRUNCATE TABLE `CONTINENT`;
TRUNCATE TABLE `MEMBER`;

SET FOREIGN_KEY_CHECKS = 1;

START TRANSACTION;

-- =====================================================
-- 1. MEMBER
-- =====================================================
INSERT INTO `MEMBER`
(`member_id`, `login_id`, `email`, `password`, `member_name`, `member_role`, `member_status`)
VALUES
    (1, 'admin_master', 'admin@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '운영 관리자', 'ADMIN', 'ACTIVE');

-- =====================================================
-- 2. INSTRUCTOR (COURSE.instructor_id 와 맞춤)
-- ADSP 대륙
-- =====================================================
INSERT INTO `MEMBER`
(`member_id`, `login_id`, `email`, `password`, `member_name`, `member_role`, `member_status`)
VALUES
    (2, 'inst_adsp_01', 'inst_adsp_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ADSP 기획관', 'INSTRUCTOR', 'ACTIVE'),
    (3, 'inst_adsp_02', 'inst_adsp_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ADSP 통계관', 'INSTRUCTOR', 'ACTIVE'),
    (4, 'inst_adsp_03', 'inst_adsp_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ADSP 실습관', 'INSTRUCTOR', 'ACTIVE'),
-- 빅데이터분석기사 대륙
    (5, 'inst_bigdata_01', 'inst_bigdata_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '빅데이터 수집관', 'INSTRUCTOR', 'ACTIVE'),
    (6, 'inst_bigdata_02', 'inst_bigdata_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '빅데이터 처리관', 'INSTRUCTOR', 'ACTIVE'),
    (7, 'inst_bigdata_03', 'inst_bigdata_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '빅데이터 시각관', 'INSTRUCTOR', 'ACTIVE'),
-- DASP 대륙
    (8, 'inst_dasp_01', 'inst_dasp_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'DASP 아키텍트', 'INSTRUCTOR', 'ACTIVE'),
    (9, 'inst_dasp_02', 'inst_dasp_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'DASP 표준관', 'INSTRUCTOR', 'ACTIVE'),
    (10, 'inst_dasp_03', 'inst_dasp_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'DASP 모델관', 'INSTRUCTOR', 'ACTIVE'),
-- 정보처리기사 대륙
    (11, 'inst_eip_01', 'inst_eip_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '정처 설계관', 'INSTRUCTOR', 'ACTIVE'),
    (12, 'inst_eip_02', 'inst_eip_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '정처 개발관', 'INSTRUCTOR', 'ACTIVE'),
    (13, 'inst_eip_03', 'inst_eip_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '정처 시스템관', 'INSTRUCTOR', 'ACTIVE'),
-- ERP 대륙
    (14, 'inst_erp_01', 'inst_erp_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ERP 회계관', 'INSTRUCTOR', 'ACTIVE'),
    (15, 'inst_erp_02', 'inst_erp_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ERP 물류관', 'INSTRUCTOR', 'ACTIVE'),
    (16, 'inst_erp_03', 'inst_erp_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'ERP 운영관', 'INSTRUCTOR', 'ACTIVE'),
-- SQLD 대륙
    (17, 'inst_sqld_01', 'inst_sqld_01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'SQLD 모델링관', 'INSTRUCTOR', 'ACTIVE'),
    (18, 'inst_sqld_02', 'inst_sqld_02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'SQLD 활용관', 'INSTRUCTOR', 'ACTIVE'),
    (19, 'inst_sqld_03', 'inst_sqld_03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', 'SQLD 튜닝관', 'INSTRUCTOR', 'ACTIVE');



INSERT INTO `MEMBER`
(`member_id`, `login_id`, `email`, `password`, `member_name`, `member_role`, `member_status`, `member_rank`)
VALUES
    (20, 'student_01', 'student01@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생01', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (21, 'student_02', 'student02@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생02', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (22, 'student_03', 'student03@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생03', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (23, 'student_04', 'student04@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생04', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (24, 'student_05', 'student05@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생05', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (25, 'student_06', 'student06@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생06', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (26, 'student_07', 'student07@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생07', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (27, 'student_08', 'student08@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생08', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (28, 'student_09', 'student09@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생09', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (29, 'student_10', 'student10@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생10', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (30, 'student_11', 'student11@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생11', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (31, 'student_12', 'student12@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생12', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (32, 'student_13', 'student13@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생13', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (33, 'student_14', 'student14@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생14', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (34, 'student_15', 'student15@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생15', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (35, 'student_16', 'student16@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생16', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (36, 'student_17', 'student17@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생17', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (37, 'student_18', 'student18@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생18', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (38, 'student_19', 'student19@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생19', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (39, 'student_20', 'student20@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생20', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (40, 'student_21', 'student21@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생21', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (41, 'student_22', 'student22@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생22', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (42, 'student_23', 'student23@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생23', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (43, 'student_24', 'student24@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생24', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (44, 'student_25', 'student25@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생25', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (45, 'student_26', 'student26@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생26', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (46, 'student_27', 'student27@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생27', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (47, 'student_28', 'student28@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생28', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (48, 'student_29', 'student29@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생29', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (49, 'student_30', 'student30@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생30', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (50, 'student_31', 'student31@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생31', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (51, 'student_32', 'student32@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생32', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (52, 'student_33', 'student33@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생33', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (53, 'student_34', 'student34@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생34', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (54, 'student_35', 'student35@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생35', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (55, 'student_36', 'student36@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생36', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (56, 'student_37', 'student37@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생37', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (57, 'student_38', 'student38@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생38', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (58, 'student_39', 'student39@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생39', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (59, 'student_40', 'student40@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생40', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (60, 'student_41', 'student41@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생41', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (61, 'student_42', 'student42@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생42', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (62, 'student_43', 'student43@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생43', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (63, 'student_44', 'student44@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생44', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (64, 'student_45', 'student45@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생45', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (65, 'student_46', 'student46@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생46', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (66, 'student_47', 'student47@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생47', 'STUDENT', 'ACTIVE', 'REPTILIAN'),
    (67, 'student_48', 'student48@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생48', 'STUDENT', 'ACTIVE', 'MINERVAL'),
    (68, 'student_49', 'student49@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생49', 'STUDENT', 'ACTIVE', 'NOVICE'),
    (69, 'student_50', 'student50@alienlms.com', '$2y$10$Rbdtg2.AI//qSqOgNXW62.Ngm4vJkwffYtrN/GFD19v12JyF8OPli', '학생50', 'STUDENT', 'ACTIVE', 'MINERVAL');


-- =====================================================
-- 2. CONTINENT
-- =====================================================
INSERT INTO `CONTINENT`
(`continent_id`, `continent_name`, `continent_description`,`continent_thumbnail_url`)
VALUES
    (1, 'ADSP 대륙', '데이터 분석 기획, 데이터 분석 기초 및 R 프로그래밍 실무를 학습하는 대륙','/images/continentThumbnail/adsp.jpg'),
    (2, '빅데이터분석기사 대륙', '빅데이터 수집, 저장, 처리 및 분석 결과 시각화를 학습하는 대륙','/images/continentThumbnail/bd.jpg'),
    (3, 'DASP 대륙', '데이터 아키텍처의 설계, 데이터 표준화 및 모델링 관리 실무를 학습하는 대륙','/images/continentThumbnail/dasp.jpg'),
    (4, '정보처리기사 대륙', '소프트웨어 설계, 개발, 데이터베이스 및 운영체제 전반을 학습하는 대륙','/images/continentThumbnail/eip.jpg'),
    (5, 'ERP 대륙', '전사적 자원 관리 시스템의 핵심 모듈과 비즈니스 프로세스를 학습하는 대륙','/images/continentThumbnail/erp.jpg'),
    (6, 'SQLD 대륙', '데이터 모델링의 이해, SQL 기본 및 활용, SQL 최적화의 원리를 학습하는 대륙','/images/continentThumbnail/sqld.jpg');


-- =====================================================
-- 3. COURSE
-- > 이미지 url은 시연 과정 확정 나면 필요하다고 판단되면 수정
-- =====================================================
INSERT INTO `COURSE`
(`course_id`, `continent_id`, `instructor_id`, `course_title`, `course_description`, `course_price`, `course_status`, `created_at`)
VALUES
-- 1. ADSP 대륙
(1, 1, 2, 'ADSP 데이터 이해와 분석 기획', '데이터 분석의 기본 개념과 ADSP 분석 기획 영역을 학습하는 강좌', 52000, 'PUBLISHED', NOW()),
(2, 1, 3, 'ADSP 통계 기초 핵심 정리', '기초 통계, 가설 검정, 상관분석 등 ADSP 통계 파트를 학습하는 강좌', 50000,'PUBLISHED', NOW()),
(3, 1, 4, 'ADSP R 실습과 문제풀이', 'R을 활용한 데이터 처리와 ADSP 실전 문제풀이를 다루는 강좌', 40000,'PUBLISHED', NOW()),
-- 2. 빅데이터분석기사 대륙
(4, 2, 5, '빅데이터 수집과 저장 구조', '빅데이터 수집 방식과 저장 구조의 기본 개념을 정리하는 강좌', 61000, 'PUBLISHED', NOW()),
(5, 2, 6, '빅데이터 처리와 분석 파이프라인', '분산 처리, 데이터 적재, 분석 파이프라인 흐름을 학습하는 강좌', 72000, 'PUBLISHED', NOW()),
(6, 2, 7, '빅데이터 시각화 실전', '빅데이터 분석 결과를 리포트와 차트로 표현하는 방법을 학습하는 강좌', 69000, 'PUBLISHED', NOW()),
-- 3. DASP 대륙
(7, 3, 8, 'DASP 데이터 아키텍처 입문', '데이터 아키텍처의 개념과 구성 요소를 이해하는 입문 강좌', 63000, 'PUBLISHED', NOW()),
(8, 3, 9, 'DASP 데이터 표준화와 관리', '데이터 표준화, 메타데이터, 품질 관리 체계를 학습하는 강좌', 71000, 'PUBLISHED', NOW()),
(9, 3, 10, 'DASP 데이터 모델링 설계', '개념/논리/물리 모델링과 설계 흐름을 학습하는 강좌', 79000, 'PUBLISHED', NOW()),
-- 4. 정보처리기사 대륙
(10, 4, 11, '정보처리기사 소프트웨어 설계', '요구사항 분석, UML, 객체지향 설계를 중심으로 학습하는 강좌', 55000, 'PUBLISHED', NOW()),
(11, 4, 12, '정보처리기사 프로그래밍과 데이터베이스', '프로그래밍 언어, SQL, 데이터베이스 핵심 개념을 학습하는 강좌', 62000, 'PUBLISHED', NOW()),
(12, 4, 13, '정보처리기사 운영체제·보안·네트워크', '운영체제, 보안, 네트워크 영역의 핵심 이론을 정리하는 강좌', 68000, 'PUBLISHED', NOW()),
-- 5. ERP 대륙
(13, 5, 14, 'ERP 회계 모듈 입문', 'ERP 회계 모듈의 흐름과 전표, 결산 구조를 학습하는 강좌', 57000, 'PUBLISHED', NOW()),
(14, 5, 15, 'ERP 인사·물류 프로세스', 'ERP에서 인사와 물류 프로세스가 어떻게 연결되는지 학습하는 강좌', 65000, 'PUBLISHED', NOW()),
(15, 5, 16, 'ERP 생산·운영 관리 실무', '생산관리와 운영관리 프로세스를 실무 흐름 중심으로 정리하는 강좌', 72000, 'PUBLISHED', NOW()),
-- 6. SQLD 대륙
(16, 6, 17, 'SQLD 데이터 모델링 핵심', 'SQLD 시험 대비를 위한 데이터 모델링 핵심 개념을 학습하는 강좌', 49000, 'PUBLISHED', NOW()),
(17, 6, 18, 'SQLD SQL 기본과 활용', 'SELECT, JOIN, 서브쿼리, 집계 함수 등 SQL 기본과 활용을 학습하는 강좌', 56000, 'PUBLISHED', NOW()),
(18, 6, 19, 'SQLD 성능 최적화와 문제풀이', '인덱스, 실행계획, SQL 튜닝과 기출형 문제풀이를 다루는 강좌', 69000, 'PUBLISHED', NOW());

-- =====================================================
-- 4. LECTURE
-- > 시연 시에 삽입할 예정
-- =====================================================
INSERT INTO `LECTURE`
(`lecture_id`, `course_id`, `lecture_title`, `lecture_description`, `lecture_order_index`, `created_at`)
VALUES
-- COURSE 1: ADSP 데이터 이해와 분석 기획
(1, 1, 'ADSP 데이터 이해 개요', '데이터와 정보의 차이, 데이터 분석의 필요성, ADSP 시험 구조를 학습하는 강의', 1, NOW()),
(2, 1, 'ADSP 분석 기획 핵심', '분석 과제 정의, 목표 수립, 분석 기획 프로세스를 학습하는 강의', 2, NOW()),

-- COURSE 2: ADSP 통계 기초 핵심 정리
(3, 2, 'ADSP 기초 통계 이론', '평균, 분산, 표준편차, 확률분포 등 통계 기초 개념을 학습하는 강의', 1, NOW()),
(4, 2, 'ADSP 가설 검정과 상관분석', '가설 검정, 유의수준, 상관분석 및 회귀 기초를 정리하는 강의', 2, NOW()),

-- COURSE 3: ADSP R 실습과 문제풀이
(5, 3, 'ADSP R 기초 실습', 'R 설치, 데이터 불러오기, 기초 문법과 데이터 처리 방법을 학습하는 강의', 1, NOW()),
(6, 3, 'ADSP R 문제풀이 실전', '기출 유형 기반 R 실습과 ADSP 실전 문제풀이를 진행하는 강의', 2, NOW()),

-- COURSE 4: 빅데이터 수집과 저장 구조
(7, 4, '빅데이터 수집 방식 이해', '정형·비정형 데이터 수집 방식과 수집 구조를 학습하는 강의', 1, NOW()),
(8, 4, '빅데이터 저장 구조 기초', '파일 시스템, 데이터 웨어하우스, 분산 저장 구조를 학습하는 강의', 2, NOW()),

-- COURSE 5: 빅데이터 처리와 분석 파이프라인
(9, 5, '빅데이터 처리 흐름', '데이터 적재, 정제, 변환 등 처리 파이프라인의 기본 흐름을 학습하는 강의', 1, NOW()),
(10, 5, '빅데이터 분석 파이프라인 실무', '분산 처리와 분석 시스템 연결 구조를 실무 관점으로 정리하는 강의', 2, NOW()),

-- COURSE 6: 빅데이터 시각화 실전
(11, 6, '빅데이터 시각화 기초', '차트 종류와 시각화 원칙, 데이터 표현 방식의 기본을 학습하는 강의', 1, NOW()),
(12, 6, '빅데이터 리포트 작성 실전', '분석 결과를 리포트와 대시보드로 표현하는 방법을 학습하는 강의', 2, NOW()),

-- COURSE 7: DASP 데이터 아키텍처 입문
(13, 7, '데이터 아키텍처 개요', '데이터 아키텍처의 정의와 역할, 구성 요소를 이해하는 강의', 1, NOW()),
(14, 7, '데이터 아키텍처 적용 흐름', '기업 시스템에서 데이터 아키텍처가 적용되는 흐름을 학습하는 강의', 2, NOW()),

-- COURSE 8: DASP 데이터 표준화와 관리
(15, 8, '데이터 표준화 기초', '용어 표준, 도메인 표준, 코드 표준 등 데이터 표준화 개념을 학습하는 강의', 1, NOW()),
(16, 8, '메타데이터와 품질 관리', '메타데이터 관리와 데이터 품질 관리 체계를 정리하는 강의', 2, NOW()),

-- COURSE 9: DASP 데이터 모델링 설계
(17, 9, '개념·논리 데이터 모델링', '개념 모델과 논리 모델의 차이 및 설계 방법을 학습하는 강의', 1, NOW()),
(18, 9, '물리 데이터 모델링 설계', '물리 모델 설계와 테이블 구조 반영 방법을 학습하는 강의', 2, NOW()),

-- COURSE 10: 정보처리기사 소프트웨어 설계
(19, 10, '요구사항 분석과 UML', '요구사항 분석 기법과 UML 다이어그램의 기본을 학습하는 강의', 1, NOW()),
(20, 10, '객체지향 설계 핵심', '객체지향 설계 원칙과 소프트웨어 설계 절차를 정리하는 강의', 2, NOW()),

-- COURSE 11: 정보처리기사 프로그래밍과 데이터베이스
(21, 11, '프로그래밍 언어 핵심 정리', '기본 문법, 자료구조, 제어문 등 프로그래밍 핵심을 학습하는 강의', 1, NOW()),
(22, 11, 'SQL과 데이터베이스 기초', '관계형 데이터베이스 개념과 SQL 기본 문법을 학습하는 강의', 2, NOW()),

-- COURSE 12: 정보처리기사 운영체제·보안·네트워크
(23, 12, '운영체제와 네트워크 기초', '프로세스, 메모리, 네트워크 구조 등 기본 이론을 학습하는 강의', 1, NOW()),
(24, 12, '보안 핵심 이론 정리', '보안 위협, 암호화, 접근 제어 등 보안 핵심 개념을 학습하는 강의', 2, NOW()),

-- COURSE 13: ERP 회계 모듈 입문
(25, 13, 'ERP 회계 모듈 구조', 'ERP 회계 모듈의 전체 흐름과 주요 기능을 학습하는 강의', 1, NOW()),
(26, 13, '전표와 결산 처리', '전표 입력, 계정 처리, 결산 구조를 실무 흐름 중심으로 학습하는 강의', 2, NOW()),

-- COURSE 14: ERP 인사·물류 프로세스
(27, 14, 'ERP 인사 프로세스 기초', '채용, 인사정보, 급여 관리 등 인사 프로세스를 학습하는 강의', 1, NOW()),
(28, 14, 'ERP 물류 프로세스 이해', '구매, 재고, 출고 등 물류 프로세스 흐름을 학습하는 강의', 2, NOW()),

-- COURSE 15: ERP 생산·운영 관리 실무
(29, 15, 'ERP 생산관리 기초', '생산 계획, 작업 지시, 공정 관리의 기본을 학습하는 강의', 1, NOW()),
(30, 15, 'ERP 운영관리 실무 흐름', '운영관리 관점에서 생산과 자원 흐름을 정리하는 강의', 2, NOW()),

-- COURSE 16: SQLD 데이터 모델링 핵심
(31, 16, 'SQLD 데이터 모델링 이론', '엔터티, 속성, 관계와 정규화의 핵심 개념을 학습하는 강의', 1, NOW()),
(32, 16, 'SQLD 데이터 모델링 문제풀이', 'SQLD 시험 대비 모델링 유형 문제를 풀이하는 강의', 2, NOW()),

-- COURSE 17: SQLD SQL 기본과 활용
(33, 17, 'SQL 기본 문법 정리', 'SELECT, WHERE, ORDER BY, GROUP BY 등 SQL 기본 문법을 학습하는 강의', 1, NOW()),
(34, 17, 'SQL 활용 실전', 'JOIN, 서브쿼리, 집계 함수 등 SQL 활용 문제를 학습하는 강의', 2, NOW()),

-- COURSE 18: SQLD 성능 최적화와 문제풀이
(35, 18, 'SQL 튜닝과 인덱스 기초', '인덱스, 실행 계획, 접근 경로 등 성능 최적화 기초를 학습하는 강의', 1, NOW()),
(36, 18, 'SQLD 기출형 문제풀이', '성능 최적화 관련 SQLD 기출형 문제를 풀이하는 강의', 2, NOW());
-- =====================================================
-- 5. COURSE_NOTICE
-- =====================================================
INSERT INTO `COURSE_NOTICE`
(`notice_id`, `course_id`, `author_id`, `notice_title`, `notice_content`, `created_at`, `updated_at`)
VALUES
    (1, 1, 2, '1주차 실습 환경 안내', 'R 문법 미리 공부해주세요.', NOW(), NOW());

-- =====================================================
-- 6. PAYMENT / PAYMENT_ITEM / ENROLLMENT

-- 시연 시에 강사가 강좌를 신청하고  -> 학생이 수강할 예정(시연 과정 중에 생김!)
-- =====================================================


-- =====================================================
-- 7. LECTURE_PROGRESS
-- ENROLLMENT.progress_rate와 대체로 맞도록 구성

-- 시연 시에 1분짜리 영상 1개를 시청하여 완강할 예정
-- =====================================================


-- =====================================================
-- 8. CONTINENT_POST / CONTINENT_COMMENT
-- > COMMENT도 굳이 넣지 않을 예정
-- =====================================================
INSERT INTO `CONTINENT_POST`
(`post_id`, `continent_id`, `author_id`, `post_title`, `post_content`, `post_is_notice`, `post_is_deleted`, `created_at`)
VALUES
    (101, 1, 60, 'ADSP 공부 시작했는데 범위가 너무 넓어요', 'ADSP 데이터 이해부터 분석 기획까지 보는데 생각보다 범위가 넓네요. 먼저 개념부터 잡고 기출로 넘어가는 방식이 괜찮을지 궁금합니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 9 DAY)),
    (102, 1, 69, '솔직히 미네르발', '미네르발이랑 노비스는 문제가 많다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (103, 1, 61, '통계 파트 공부 순서 추천 부탁드립니다', '기초 통계, 확률분포, 가설검정 순으로 공부하려고 하는데 혹시 더 효율적인 순서가 있을까요? 강의 듣는 순서도 같이 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 8 DAY)),
    (104, 1, 62, 'ADSP 기출문제는 언제부터 푸는 게 좋을까요?', '이론을 한 번 다 돌리고 기출을 풀지, 아니면 단원별로 바로바로 기출을 병행할지 고민됩니다. 먼저 합격한 분들 의견 듣고 싶어요.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 7 DAY)),
    (105, 1, 63, 'R 실습 환경 세팅 질문 있습니다', 'R이랑 RStudio 설치는 했는데 패키지 설치 과정에서 자꾸 막히네요. ADSP 실습용으로 꼭 필요한 패키지만 먼저 정리해주실 수 있나요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 6 DAY)),
    (106, 1, 64, '분석 기획 파트가 제일 헷갈립니다', 'CRISP-DM이랑 KDD, 그리고 분석 과제 정의 부분이 서로 비슷하게 느껴져서 정리가 안 됩니다. 외우는 팁이나 구분법 있으면 알려주세요.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (107, 1, 65, 'ADSP 비전공자도 한 달 안에 가능할까요?', 'SQL이나 통계 배경지식이 거의 없는 상태인데 하루 2~3시간 정도 투자하면 한 달 안에 합격권까지 갈 수 있을지 현실적인 조언 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 4 DAY)),
    (108, 1, 66, '요약 노트 공유 방식 어떻게 하시나요?', '강의 들으면서 필기한 내용을 따로 정리 중인데, 다들 개념 위주로 정리하는지 문제 유형 위주로 정리하는지 궁금합니다. 효율적인 복습법이 필요해요.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (109, 1, 67, '모의고사 점수가 잘 안 오릅니다', '처음에는 50점대였는데 계속 비슷한 점수만 나오네요. 틀린 문제를 다시 보는 방식이 문제인지, 아니면 개념 복습이 부족한 건지 모르겠습니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (110, 1, 68, '시험 직전 마무리 전략 같이 공유해요', '시험이 얼마 안 남아서 이제는 새로운 개념보다 오답 정리랑 빈출 개념 위주로 보려고 합니다. 마지막 일주일 공부 루틴 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY));


-- =====================================================
-- 9. QNA
-- > QNA도 굳이 넣어두지 않고 영상 녹화에 내용으로 담을 예정
-- =====================================================


-- =====================================================
-- 10. CART
-- 시연 시에 담을 예정
-- =====================================================


-- =====================================================
-- 11. REPORT
-- 시연 시에 담을 예정
-- =====================================================


-- =====================================================
-- 12. REFUND
-- 시연 시에 담을 예정
-- =====================================================

-- =====================================================
-- 6. PAYMENT / PAYMENT_ITEM / ENROLLMENT
-- 대륙별 수강 학생 수가 서로 다르게 보이도록 분포 조정
-- ADSP 8명 / 빅데이터 6명 / DASP 5명 / 정보처리기사 7명 / ERP 4명 / SQLD 9명
-- =====================================================

INSERT INTO `PAYMENT`
(`payment_id`, `member_id`, `payment_order_no`, `payment_total_price`, `payment_completed_at`, `toss_payment_key`)
VALUES
    (2, 21, 'ORD-20260421-0002', 50000,  DATE_SUB(NOW(), INTERVAL 29 DAY), 'test_toss_key_0002'),
    (3, 22, 'ORD-20260421-0003', 130000, DATE_SUB(NOW(), INTERVAL 28 DAY), 'test_toss_key_0003'),
    (4, 23, 'ORD-20260421-0004', 63000,  DATE_SUB(NOW(), INTERVAL 27 DAY), 'test_toss_key_0004'),
    (5, 24, 'ORD-20260421-0005', 128000, DATE_SUB(NOW(), INTERVAL 26 DAY), 'test_toss_key_0005'),
    (6, 25, 'ORD-20260421-0006', 117000, DATE_SUB(NOW(), INTERVAL 25 DAY), 'test_toss_key_0006'),
    (7, 26, 'ORD-20260421-0007', 57000,  DATE_SUB(NOW(), INTERVAL 24 DAY), 'test_toss_key_0007'),
    (8, 27, 'ORD-20260421-0008', 137000, DATE_SUB(NOW(), INTERVAL 23 DAY), 'test_toss_key_0008'),
    (9, 28, 'ORD-20260421-0009', 125000, DATE_SUB(NOW(), INTERVAL 22 DAY), 'test_toss_key_0009'),
    (10, 29, 'ORD-20260421-0010', 72000, DATE_SUB(NOW(), INTERVAL 21 DAY), 'test_toss_key_0010'),
    (11, 30, 'ORD-20260421-0011', 40000, DATE_SUB(NOW(), INTERVAL 20 DAY), 'test_toss_key_0011'),
    (12, 31, 'ORD-20260421-0012', 71000, DATE_SUB(NOW(), INTERVAL 19 DAY), 'test_toss_key_0012'),
    (13, 32, 'ORD-20260421-0013', 68000, DATE_SUB(NOW(), INTERVAL 18 DAY), 'test_toss_key_0013'),
    (14, 33, 'ORD-20260421-0014', 125000, DATE_SUB(NOW(), INTERVAL 17 DAY), 'test_toss_key_0014'),
    (15, 34, 'ORD-20260421-0015', 61000, DATE_SUB(NOW(), INTERVAL 16 DAY), 'test_toss_key_0015'),
    (16, 35, 'ORD-20260421-0016', 49000, DATE_SUB(NOW(), INTERVAL 15 DAY), 'test_toss_key_0016'),
    (17, 36, 'ORD-20260421-0017', 105000, DATE_SUB(NOW(), INTERVAL 14 DAY), 'test_toss_key_0017'),
    (18, 37, 'ORD-20260421-0018', 62000, DATE_SUB(NOW(), INTERVAL 13 DAY), 'test_toss_key_0018'),
    (19, 38, 'ORD-20260421-0019', 72000, DATE_SUB(NOW(), INTERVAL 12 DAY), 'test_toss_key_0019'),
    (20, 39, 'ORD-20260421-0020', 148000, DATE_SUB(NOW(), INTERVAL 11 DAY), 'test_toss_key_0020'),
    (21, 40, 'ORD-20260421-0021', 129000, DATE_SUB(NOW(), INTERVAL 10 DAY), 'test_toss_key_0021'),
    (22, 41, 'ORD-20260421-0022', 68000, DATE_SUB(NOW(), INTERVAL 9 DAY),  'test_toss_key_0022'),
    (23, 42, 'ORD-20260421-0023', 52000, DATE_SUB(NOW(), INTERVAL 8 DAY),  'test_toss_key_0023'),
    (24, 43, 'ORD-20260421-0024', 111000, DATE_SUB(NOW(), INTERVAL 7 DAY),  'test_toss_key_0024'),
    (25, 44, 'ORD-20260421-0025', 71000, DATE_SUB(NOW(), INTERVAL 6 DAY),  'test_toss_key_0025'),
    (26, 45, 'ORD-20260421-0026', 40000, DATE_SUB(NOW(), INTERVAL 5 DAY),  'test_toss_key_0026'),
    (27, 46, 'ORD-20260421-0027', 102000, DATE_SUB(NOW(), INTERVAL 4 DAY),  'test_toss_key_0027'),
    (28, 47, 'ORD-20260421-0028', 61000, DATE_SUB(NOW(), INTERVAL 3 DAY),  'test_toss_key_0028'),
    (29, 48, 'ORD-20260421-0029', 111000, DATE_SUB(NOW(), INTERVAL 2 DAY),  'test_toss_key_0029'),
    (30, 49, 'ORD-20260421-0030', 50000, DATE_SUB(NOW(), INTERVAL 1 DAY),  'test_toss_key_0030'),
    (31, 69, 'ORD-20260421-0031', 69000, NOW(), 'test_toss_key_0031');

INSERT INTO `PAYMENT_ITEM`
(`payment_item_id`, `payment_id`, `course_id`, `item_price_at_purchase`)
VALUES

    (3, 2, 2, 50000),

    (4, 3, 4, 61000),
    (5, 3, 6, 69000),

    (6, 4, 7, 63000),

    (7, 5, 9, 79000),
    (8, 5, 16, 49000),

    (9, 6, 10, 55000),
    (10, 6, 11, 62000),

    (11, 7, 13, 57000),

    (12, 8, 14, 65000),
    (13, 8, 15, 72000),

    (14, 9, 17, 56000),
    (15, 9, 18, 69000),

    (16, 10, 5, 72000),

    (17, 11, 3, 40000),

    (18, 12, 8, 71000),

    (19, 13, 12, 68000),

    (20, 14, 6, 69000),
    (21, 14, 17, 56000),

    (22, 15, 4, 61000),

    (23, 16, 16, 49000),

    (24, 17, 2, 50000),
    (25, 17, 10, 55000),

    (26, 18, 11, 62000),

    (27, 19, 15, 72000),

    (28, 20, 9, 79000),
    (29, 20, 18, 69000),

    (30, 21, 5, 72000),
    (31, 21, 13, 57000),

    (32, 22, 12, 68000),

    (33, 23, 1, 52000),

    (34, 24, 10, 55000),
    (35, 24, 17, 56000),

    (36, 25, 8, 71000),

    (37, 26, 3, 40000),

    (38, 27, 1, 52000),
    (39, 27, 2, 50000),

    (40, 28, 4, 61000),

    (41, 29, 11, 62000),
    (42, 29, 16, 49000),

    (43, 30, 2, 50000),

    (44, 31, 18, 69000);

INSERT INTO `ENROLLMENT`
(`enrollment_id`, `member_id`, `course_id`, `enrollment_status`, `enrollment_progress_rate`, `created_at`)
VALUES

    (3, 21, 2, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 29 DAY)),

    (4, 22, 4, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 28 DAY)),
    (5, 22, 6, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 28 DAY)),

    (6, 23, 7, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 27 DAY)),

    (7, 24, 9, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 26 DAY)),
    (8, 24, 16, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 26 DAY)),

    (9, 25, 10, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 25 DAY)),
    (10, 25, 11, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 25 DAY)),

    (11, 26, 13, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 24 DAY)),

    (12, 27, 14, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 23 DAY)),
    (13, 27, 15, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 23 DAY)),

    (14, 28, 17, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 22 DAY)),
    (15, 28, 18, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 22 DAY)),

    (16, 29, 5, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 21 DAY)),

    (17, 30, 3, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 20 DAY)),

    (18, 31, 8, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 19 DAY)),

    (19, 32, 12, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 18 DAY)),

    (20, 33, 6, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 17 DAY)),
    (21, 33, 17, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 17 DAY)),

    (22, 34, 4, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 16 DAY)),

    (23, 35, 16, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 15 DAY)),

    (24, 36, 2, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 14 DAY)),
    (25, 36, 10, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 14 DAY)),

    (26, 37, 11, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 13 DAY)),

    (27, 38, 15, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 12 DAY)),

    (28, 39, 9, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 11 DAY)),
    (29, 39, 18, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 11 DAY)),

    (30, 40, 5, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (31, 40, 13, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 10 DAY)),

    (32, 41, 12, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 9 DAY)),

    (33, 42, 1, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 8 DAY)),

    (34, 43, 10, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 7 DAY)),
    (35, 43, 17, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 7 DAY)),

    (36, 44, 8, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 6 DAY)),

    (37, 45, 3, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 5 DAY)),

    (38, 46, 1, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 4 DAY)),
    (39, 46, 2, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 4 DAY)),

    (40, 47, 4, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 3 DAY)),

    (41, 48, 11, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (42, 48, 16, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 2 DAY)),

    (43, 49, 2, 'ACTIVE', 0.00, DATE_SUB(NOW(), INTERVAL 1 DAY)),

    (44, 69, 18, 'ACTIVE', 0.00, NOW());

-- =====================================================
-- 8. CONTINENT_POST
-- 기존 ADSP(1번 대륙) 게시글은 유지
-- 다른 대륙 게시글 수는 서로 다르게 구성
-- =====================================================

INSERT INTO `CONTINENT_POST`
(`post_id`, `continent_id`, `author_id`, `post_title`, `post_content`, `post_is_notice`, `post_is_deleted`, `created_at`)
VALUES
-- 2. 빅데이터분석기사 대륙 (5개)
(111, 2, 20, '빅데이터 수집 파트에서 Kafka까지 봐야 하나요?', '빅데이터 수집과 저장 구조 강의를 듣고 있는데 시험 대비 기준으로 Kafka 같은 도구까지 깊게 알아야 하는지 궁금합니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(112, 2, 21, '저장 구조 개념이 자꾸 헷갈립니다', '데이터 레이크, 웨어하우스, 분산 저장 구조 차이를 정리해도 자꾸 섞입니다. 암기 팁 있으신 분 계신가요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(113, 2, 22, '분산 파일 시스템 정리 자료 있으신 분?', '하둡 계열 개념이 처음이라 용어가 헷갈립니다. 강의 기준으로 먼저 익혀야 할 핵심 키워드 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(114, 2, 23, '시각화보다 처리 파트가 더 어렵네요', '분산 처리 흐름이 머릿속에서 안 그려집니다. 수집 -> 저장 -> 처리 -> 분석 흐름을 공부할 때 어떤 식으로 정리하면 좋을까요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(115, 2, 24, '오픈채팅으로 요약본 판매합니다', '빅데이터분석기사 요약본 정리해둔 거 있습니다. 관심 있으신 분은 오픈채팅으로 연락 주세요.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 3. DASP 대륙 (3개)
(116, 3, 25, 'DASP 표준화 용어 정리 팁 있을까요?', '데이터 표준화 파트에서 용어, 도메인, 코드 표준이 비슷하게 느껴져서 구분이 어렵습니다. 정리 방법 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(117, 3, 26, '모델링이랑 아키텍처 경계가 헷갈립니다', 'DASP 데이터 아키텍처 입문 강좌를 듣는 중인데 모델링과 아키텍처의 역할 차이를 어떻게 이해하면 좋을지 질문드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(118, 3, 27, '메타데이터 관리 사례 같이 봐요', '실무에서 메타데이터를 어떻게 관리하는지 궁금합니다. 강의 외에 이해하기 쉬운 사례가 있으면 공유 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 4. 정보처리기사 대륙 (6개)
(119, 4, 28, '정보처리기사 설계 파트 공부 순서 추천', '요구사항 분석, UML, 객체지향 설계가 연결되는 순서를 잡고 싶은데 강의 수강 순서 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(120, 4, 29, 'UML 다이어그램 암기 팁 있나요', '유스케이스, 클래스, 시퀀스 다이어그램이 자꾸 섞입니다. 시험 기준으로 꼭 구분해야 하는 포인트가 궁금합니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(121, 4, 30, '운영체제랑 네트워크 비중 어떻게 가져가세요?', '운영체제, 보안, 네트워크를 한꺼번에 공부하려니 분량이 많습니다. 다들 어떤 비율로 공부하시는지 궁금합니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(122, 4, 31, '데이터베이스 파트 문제풀이 같이 하실 분', 'SQL과 데이터베이스 기초 강의 듣는 분들 중에 같이 기출 문제풀이 하실 분 있으면 댓글 주세요.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(123, 4, 32, '시험 요약본 유료 공유합니다', '정처기 요약본 직접 정리했습니다. 필요한 분은 개인적으로 연락 주세요. 유료입니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(124, 4, 33, '보안 파트는 기출 반복이 답일까요', '보안 용어가 많아서 정리가 어렵습니다. 이론 정독보다 기출 반복이 더 효율적인지 조언 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 5. ERP 대륙 (4개)
(125, 5, 34, 'ERP 회계 모듈 용어가 낯섭니다', '전표, 결산, 원장 같은 기본 용어부터 정리하고 싶은데 입문자 기준으로 어떤 순서가 좋을까요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(126, 5, 35, '물류 프로세스 흐름도 같이 정리해봐요', '구매 -> 입고 -> 재고 -> 출고 흐름이 강의에서는 이해되는데 정리해놓고 보면 헷갈립니다. 같이 정리해보실 분 계신가요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(127, 5, 36, '특정 강사 강의 진짜 별로네요', '내용보다 말투가 더 거슬려서 집중이 안 됩니다. 솔직히 이 강의 추천하고 싶지 않습니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(128, 5, 37, '생산관리 파트 실무 예시가 있으면 좋겠어요', '생산관리 기초 강의를 듣고 있는데 실제 회사에서 어떤 식으로 쓰이는지 예시가 더 있으면 이해가 쉬울 것 같습니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 6. SQLD 대륙 (2개)
(129, 6, 38, 'SQLD JOIN 문제만 모아서 풀고 싶습니다', 'JOIN 쪽에서 계속 틀리는데 SQL 기본과 활용 강좌 기준으로 어떤 유형을 먼저 잡아야 할까요?', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(130, 6, 39, '실행계획 보는 순서 정리해주실 분?', '성능 최적화 파트에서 실행계획을 볼 때 어디부터 읽어야 하는지 감이 잘 안 옵니다. 공부 순서 추천 부탁드립니다.', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- =====================================================
-- 9. QNA
-- 조건: 해당 강좌를 듣는 수강생만 질문 작성
-- 답변은 넣지 않음(parent_id = NULL)
-- =====================================================

INSERT INTO `QNA`
(`qna_id`, `course_id`, `author_id`, `parent_id`, `qna_title`, `qna_content`, `qna_is_deleted`, `created_at`)
VALUES
    (1001, 1, 20, NULL, '분석 기획 파트에서 KPI와 과제 정의 구분 질문', '강의에서는 KPI와 분석 과제 정의를 구분해서 설명해주시는데 실제 문제에서는 비슷하게 보여 헷갈립니다. 구분 포인트가 있을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 14 DAY)),
    (1002, 2, 21, NULL, '가설검정 문제풀이 접근 순서가 궁금합니다', '통계 문제를 풀 때 귀무가설 세우기부터 p-value 해석까지 순서를 자꾸 놓칩니다. 문제 접근 순서를 알려주실 수 있을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 13 DAY)),
    (1003, 4, 22, NULL, '수집과 저장 구조를 같이 외워도 되나요?', '빅데이터 수집과 저장 구조가 연결되어 있어서 한 번에 정리하고 싶은데 시험 대비에도 그렇게 공부해도 괜찮을지 궁금합니다.', FALSE, DATE_SUB(NOW(), INTERVAL 12 DAY)),
    (1004, 7, 23, NULL, '데이터 아키텍처와 데이터 모델링 차이', 'DASP 데이터 아키텍처 입문 강의에서 전체 구조 설명은 이해했는데 모델링과의 역할 차이를 한 문장으로 정리하고 싶습니다.', FALSE, DATE_SUB(NOW(), INTERVAL 11 DAY)),
    (1005, 9, 24, NULL, '논리 모델에서 정규화 적용 시점 질문', '개념 모델에서 바로 정규화를 생각해야 하는지, 논리 모델 단계에서 본격적으로 보는지 흐름이 궁금합니다.', FALSE, DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (1006, 10, 25, NULL, 'UML 다이어그램 문제 접근법 질문', '요구사항 분석과 UML 강의 수강 중인데 다이어그램 문제에서 먼저 봐야 하는 키워드가 있는지 궁금합니다.', FALSE, DATE_SUB(NOW(), INTERVAL 9 DAY)),
    (1007, 13, 26, NULL, '전표 처리 흐름이 한 번에 안 잡힙니다', 'ERP 회계 모듈 입문 강의에서 전표 입력 후 결산으로 이어지는 흐름을 다시 듣고 있는데 핵심 단계만 요약해서 이해하고 싶습니다.', FALSE, DATE_SUB(NOW(), INTERVAL 8 DAY)),
    (1008, 14, 27, NULL, '물류 프로세스에서 구매와 재고 연결 질문', 'ERP 인사·물류 프로세스 강의에서 구매, 입고, 재고 연결이 나오는데 실무 흐름처럼 이해하려면 어디를 중심으로 봐야 할까요?', FALSE, DATE_SUB(NOW(), INTERVAL 7 DAY)),
    (1009, 17, 28, NULL, 'JOIN 문제에서 ON절과 WHERE절 차이 질문', 'SQL 기본과 활용 강의를 듣다가 JOIN 문제를 풀고 있는데 OUTER JOIN에서 ON절과 WHERE절 차이가 특히 헷갈립니다. 시험 기준으로 어떻게 정리하면 좋을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 6 DAY)),
    (1010, 5, 29, NULL, '분산 처리 파트 정리 순서 질문', '빅데이터 처리와 분석 파이프라인 강의에서 적재, 정제, 변환, 분석 순서를 정리하고 있는데 각 단계 핵심 키워드를 어떻게 묶어 외우면 좋을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (1011, 3, 30, NULL, 'R 실습에서 자주 쓰는 함수만 먼저 봐도 될까요?', 'ADSP R 실습과 문제풀이 강좌를 듣고 있는데 처음부터 전체 문법을 다 보기보다 시험에 자주 나오는 함수만 먼저 정리해도 괜찮을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 4 DAY)),
    (1012, 12, 32, NULL, '운영체제·보안·네트워크 요약 학습 질문', '운영체제·보안·네트워크 강의를 듣고 있는데 세 영역을 균형 있게 공부하려면 어떤 식으로 회독을 나누는 게 좋을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (1013, 6, 33, NULL, '시각화 차트 선택 기준 질문', '막대차트, 선차트, 원차트 선택 기준을 강의 기준으로 다시 정리하고 싶습니다. 시험이나 실무에서 헷갈리지 않게 외우는 방법이 있을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 3 DAY)),
    (1014, 16, 35, NULL, '정규화와 반정규화 문제풀이 방향 질문', 'SQLD 데이터 모델링 핵심 강의 수강 중인데 정규화와 반정규화 문제를 풀 때 기준이 자꾸 흔들립니다. 접근 기준을 알려주실 수 있을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (1015, 11, 48, NULL, '강의 외부 요약본으로만 공부해도 될까요?', '프로그래밍과 데이터베이스 강의를 듣고 있는데 외부 요약본만으로도 커버가 되는지 궁금합니다. 강의와 병행해야 할까요?', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (1016, 17, 43, NULL, '기출 PDF 공유 가능한 분 있나요?', 'SQLD SQL 기본과 활용 강의를 듣고 있는데 외부에서 돌아다니는 기출 PDF를 같이 보고 싶습니다. 공유받을 수 있는 방법이 있을까요?', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- =====================================================
-- 11. REPORT
-- 마지막에 작성
-- =====================================================

INSERT INTO `REPORT`
(`report_id`, `reporter_id`, `report_target_type`, `report_target_id`, `report_reason`, `created_at`)
VALUES
    (1, 42, 'CONTINENT_POST', 102, '회원 등급을 비하하는 표현이 포함되어 있어 신고합니다.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
    (2, 43, 'CONTINENT_POST', 115, '오픈채팅 유도 및 자료 판매성 게시글로 보여 신고합니다.', DATE_SUB(NOW(), INTERVAL 20 HOUR)),
    (3, 44, 'CONTINENT_POST', 123, '시험 요약본 유료 판매/홍보성 게시글로 보여 검토 요청드립니다.', DATE_SUB(NOW(), INTERVAL 16 HOUR)),
    (4, 45, 'CONTINENT_POST', 127, '강의 내용과 무관한 특정 강사 비방성 글로 보여 신고합니다.', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
    (5, 46, 'QNA', 1015, '외부 요약본만으로 수강을 대체하려는 취지로 보여 검토 요청드립니다.', DATE_SUB(NOW(), INTERVAL 10 HOUR)),
    (6, 47, 'QNA', 1016, '기출 PDF 공유 요청은 저작권 이슈가 우려되어 신고합니다.', DATE_SUB(NOW(), INTERVAL 8 HOUR));

COMMIT;

SET FOREIGN_KEY_CHECKS = 1;