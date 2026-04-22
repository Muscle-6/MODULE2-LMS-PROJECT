-- =====================================
-- module02_lms에서 실행
-- ======================================


DROP DATABASE IF EXISTS module02_lms;
CREATE DATABASE module02_lms
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

DROP USER IF EXISTS 'Alien'@'localhost';
CREATE USER 'Alien'@'localhost' IDENTIFIED BY 'Alien';
GRANT ALL PRIVILEGES ON module02_lms.* TO 'Alien'@'localhost';
FLUSH PRIVILEGES;

USE module02_lms;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `REFUND`;
DROP TABLE IF EXISTS `REPORT`;
DROP TABLE IF EXISTS `LECTURE_PROGRESS`;
DROP TABLE IF EXISTS `QNA`;
DROP TABLE IF EXISTS `ENROLLMENT`;
DROP TABLE IF EXISTS `CART`;
DROP TABLE IF EXISTS `PAYMENT_ITEM`;
DROP TABLE IF EXISTS `PAYMENT`;
DROP TABLE IF EXISTS `CONTINENT_COMMENT`;
DROP TABLE IF EXISTS `CONTINENT_POST`;
DROP TABLE IF EXISTS `COURSE_NOTICE`;
DROP TABLE IF EXISTS `LECTURE`;
DROP TABLE IF EXISTS `COURSE`;
DROP TABLE IF EXISTS `CONTINENT`;
DROP TABLE IF EXISTS `MEMBER`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `MEMBER` (
                          `member_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `login_id` VARCHAR(50) NOT NULL,
                          `email` VARCHAR(100) NOT NULL,
                          `password` VARCHAR(255) NOT NULL,
                          `member_name` VARCHAR(50) NOT NULL,
                          `phone` VARCHAR(20) NULL,
                          `profile_image_url` VARCHAR(500) NULL,
                          `member_role` ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
                          `member_status` ENUM('ACTIVE', 'INACTIVE', 'BANNED') NOT NULL DEFAULT 'ACTIVE',
                          `introduction` TEXT NULL,
                          `member_rank` ENUM('REPTILIAN', 'MINERVAL', 'NOVICE') NULL,
                          `last_login_at` DATETIME NULL,
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `deleted_at` DATETIME NULL,
                          CONSTRAINT `PK_MEMBER` PRIMARY KEY (`member_id`),
                          CONSTRAINT `UK_MEMBER_LOGIN_ID` UNIQUE (`login_id`),
                          CONSTRAINT `UK_MEMBER_EMAIL` UNIQUE (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `CONTINENT` (
                             `continent_id` BIGINT NOT NULL AUTO_INCREMENT,
                             `continent_name` VARCHAR(100) NOT NULL,
                             `continent_description` TEXT NULL,
                             `continent_thumbnail_url` VARCHAR(500) NULL,
                             CONSTRAINT `PK_CONTINENT` PRIMARY KEY (`continent_id`),
                             CONSTRAINT `UK_CONTINENT_NAME` UNIQUE (`continent_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `COURSE` (
                          `course_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `continent_id` BIGINT NOT NULL,
                          `instructor_id` BIGINT NOT NULL,
                          `course_title` VARCHAR(200) NOT NULL,
                          `course_description` TEXT NULL,
                          `course_thumbnail_url` VARCHAR(500) NULL,
                          `course_price` INT NOT NULL,
                          `course_status` ENUM('PUBLISHED', 'DRAFT') NOT NULL DEFAULT 'DRAFT',
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT `PK_COURSE` PRIMARY KEY (`course_id`),
                          CONSTRAINT `FK_COURSE_CONTINENT` FOREIGN KEY (`continent_id`)
                              REFERENCES `CONTINENT` (`continent_id`),
                          CONSTRAINT `FK_COURSE_MEMBER` FOREIGN KEY (`instructor_id`)
                              REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `LECTURE` (
                           `lecture_id`          BIGINT NOT NULL AUTO_INCREMENT,
                           `course_id`           BIGINT NOT NULL,
                           `lecture_title`       VARCHAR(200) NOT NULL,
                           `lecture_description` TEXT NULL,
                           `lecture_order_index` INT NOT NULL,
                           `video_url`           VARCHAR(500) NULL,
                           `video_status`        ENUM('NONE','UPLOADING','READY') NOT NULL DEFAULT 'READY',
                           `created_at`          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT `PK_LECTURE`             PRIMARY KEY (`lecture_id`),
                           CONSTRAINT `FK_LECTURE_COURSE`      FOREIGN KEY (`course_id`) REFERENCES `COURSE` (`course_id`),
                           CONSTRAINT `UK_LECTURE_COURSE_ORDER` UNIQUE (`course_id`, `lecture_order_index`)
) ENGINE=InnoDB;


CREATE TABLE `COURSE_NOTICE` (
                                 `notice_id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `course_id` BIGINT NOT NULL,
                                 `author_id` BIGINT NOT NULL,
                                 `notice_title` VARCHAR(200) NOT NULL,
                                 `notice_content` TEXT NOT NULL,
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT `PK_COURSE_NOTICE` PRIMARY KEY (`notice_id`),
                                 CONSTRAINT `FK_COURSE_NOTICE_COURSE` FOREIGN KEY (`course_id`)
                                     REFERENCES `COURSE` (`course_id`),
                                 CONSTRAINT `FK_COURSE_NOTICE_MEMBER` FOREIGN KEY (`author_id`)
                                     REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `CONTINENT_POST` (
                                  `post_id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `continent_id` BIGINT NOT NULL,
                                  `author_id` BIGINT NOT NULL,
                                  `post_title` VARCHAR(200) NOT NULL,
                                  `post_content` TEXT NOT NULL,
                                  `post_is_notice` BOOLEAN NOT NULL DEFAULT FALSE,
                                  `post_is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
                                  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT `PK_CONTINENT_POST` PRIMARY KEY (`post_id`),
                                  CONSTRAINT `FK_CONTINENT_POST_CONTINENT` FOREIGN KEY (`continent_id`)
                                      REFERENCES `CONTINENT` (`continent_id`),
                                  CONSTRAINT `FK_CONTINENT_POST_MEMBER` FOREIGN KEY (`author_id`)
                                      REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `CONTINENT_COMMENT` (
                                     `comment_id` BIGINT NOT NULL AUTO_INCREMENT,
                                     `post_id` BIGINT NOT NULL,
                                     `author_id` BIGINT NOT NULL,
                                     `comment_content` TEXT NOT NULL,
                                     `comment_is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
                                     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT `PK_CONTINENT_COMMENT` PRIMARY KEY (`comment_id`),
                                     CONSTRAINT `FK_CONTINENT_COMMENT_POST` FOREIGN KEY (`post_id`)
                                         REFERENCES `CONTINENT_POST` (`post_id`),
                                     CONSTRAINT `FK_CONTINENT_COMMENT_MEMBER` FOREIGN KEY (`author_id`)
                                         REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `PAYMENT` (
                           `payment_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `member_id` BIGINT NOT NULL,
                           `payment_order_no` VARCHAR(100) NOT NULL,
                           `payment_total_price` INT NOT NULL,
                           `payment_completed_at` DATETIME NULL,
                           `toss_payment_key` VARCHAR(200) NULL,
                           CONSTRAINT `PK_PAYMENT` PRIMARY KEY (`payment_id`),
                           CONSTRAINT `UK_PAYMENT_ORDER_NO` UNIQUE (`payment_order_no`),
                           CONSTRAINT `FK_PAYMENT_MEMBER` FOREIGN KEY (`member_id`)
                               REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `PAYMENT_ITEM` (
                                `payment_item_id` BIGINT NOT NULL AUTO_INCREMENT,
                                `payment_id` BIGINT NOT NULL,
                                `course_id` BIGINT NOT NULL,
                                `item_price_at_purchase` INT NOT NULL,
                                CONSTRAINT `PK_PAYMENT_ITEM` PRIMARY KEY (`payment_item_id`),
                                CONSTRAINT `FK_PAYMENT_ITEM_PAYMENT` FOREIGN KEY (`payment_id`)
                                    REFERENCES `PAYMENT` (`payment_id`),
                                CONSTRAINT `FK_PAYMENT_ITEM_COURSE` FOREIGN KEY (`course_id`)
                                    REFERENCES `COURSE` (`course_id`),
                                CONSTRAINT `UK_PAYMENT_ITEM_COMPOSITE` UNIQUE (`payment_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `CART` (
                        `cart_id` BIGINT NOT NULL AUTO_INCREMENT,
                        `course_id` BIGINT NOT NULL,
                        `member_id` BIGINT NOT NULL,
                        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT `PK_CART` PRIMARY KEY (`cart_id`),
                        CONSTRAINT `FK_CART_COURSE` FOREIGN KEY (`course_id`)
                            REFERENCES `COURSE` (`course_id`),
                        CONSTRAINT `FK_CART_MEMBER` FOREIGN KEY (`member_id`)
                            REFERENCES `MEMBER` (`member_id`),
                        CONSTRAINT `UK_CART_COMPOSITE` UNIQUE (`course_id`, `member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ENROLLMENT` (
                              `enrollment_id` BIGINT NOT NULL AUTO_INCREMENT,
                              `member_id` BIGINT NOT NULL,
                              `course_id` BIGINT NOT NULL,
                              `enrollment_status` ENUM('ACTIVE', 'REFUNDED') NOT NULL DEFAULT 'ACTIVE',
                              `enrollment_progress_rate` DECIMAL(5,2) NULL DEFAULT 0.00,
                              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT `PK_ENROLLMENT` PRIMARY KEY (`enrollment_id`),
                              CONSTRAINT `FK_ENROLLMENT_MEMBER` FOREIGN KEY (`member_id`)
                                  REFERENCES `MEMBER` (`member_id`),
                              CONSTRAINT `FK_ENROLLMENT_COURSE` FOREIGN KEY (`course_id`)
                                  REFERENCES `COURSE` (`course_id`),
                              CONSTRAINT `UK_ENROLLMENT_MEMBER_COURSE` UNIQUE (`member_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `LECTURE_PROGRESS` (
                                    `progress_id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `member_id` BIGINT NOT NULL,
                                    `lecture_id` BIGINT NOT NULL,
                                    `progress_is_completed` BOOLEAN NOT NULL DEFAULT FALSE,
                                    CONSTRAINT `PK_LECTURE_PROGRESS` PRIMARY KEY (`progress_id`),
                                    CONSTRAINT `FK_PROGRESS_MEMBER` FOREIGN KEY (`member_id`)
                                        REFERENCES `MEMBER` (`member_id`),
                                    CONSTRAINT `FK_PROGRESS_LECTURE` FOREIGN KEY (`lecture_id`)
                                        REFERENCES `LECTURE` (`lecture_id`) ON DELETE CASCADE,
                                    CONSTRAINT `UK_PROGRESS_MEMBER_LECTURE` UNIQUE (`member_id`, `lecture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `QNA` (
                       `qna_id` BIGINT NOT NULL AUTO_INCREMENT,
                       `course_id` BIGINT NOT NULL,
                       `author_id` BIGINT NOT NULL,
                       `parent_id` BIGINT NULL,
                       `qna_title` VARCHAR(200) NULL,
                       `qna_content` TEXT NOT NULL,
                       `qna_is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
                       `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT `PK_QNA` PRIMARY KEY (`qna_id`),
                       CONSTRAINT `FK_QNA_COURSE` FOREIGN KEY (`course_id`)
                           REFERENCES `COURSE` (`course_id`),
                       CONSTRAINT `FK_QNA_MEMBER` FOREIGN KEY (`author_id`)
                           REFERENCES `MEMBER` (`member_id`),
                       CONSTRAINT `FK_QNA_PARENT` FOREIGN KEY (`parent_id`)
                           REFERENCES `QNA` (`qna_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REPORT` (
                          `report_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `reporter_id` BIGINT NOT NULL,
                          `report_target_type` ENUM('CONTINENT_POST', 'CONTINENT_COMMENT', 'QNA') NOT NULL,
                          `report_target_id` BIGINT NOT NULL,
                          `report_reason` TEXT NOT NULL,
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT `PK_REPORT` PRIMARY KEY (`report_id`),
                          CONSTRAINT `FK_REPORT_MEMBER` FOREIGN KEY (`reporter_id`)
                              REFERENCES `MEMBER` (`member_id`),
                          INDEX `IDX_REPORT_TARGET` (`report_target_type`, `report_target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE report ADD COLUMN reported_title VARCHAR(255);
ALTER TABLE report ADD COLUMN reported_content TEXT;

CREATE TABLE `REFUND` (
                          `refund_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `payment_id` BIGINT NOT NULL,
                          `member_id` BIGINT NOT NULL,
                          `refund_reason` TEXT NULL,
                          `refund_status` ENUM('REQUESTED', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'REQUESTED',
                          `refund_requested_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `refund_processed_at` DATETIME NULL,
                          CONSTRAINT `PK_REFUND` PRIMARY KEY (`refund_id`),
                          CONSTRAINT `UK_REFUND_PAYMENT` UNIQUE (`payment_id`),  -- 여기 수정됨
                          CONSTRAINT `FK_REFUND_PAYMENT` FOREIGN KEY (`payment_id`) -- 여기 수정됨
                              REFERENCES `PAYMENT` (`payment_id`), -- 참조 테이블도 PAYMENT로 변경
                          CONSTRAINT `FK_REFUND_MEMBER` FOREIGN KEY (`member_id`)
                              REFERENCES `MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
