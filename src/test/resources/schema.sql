-- MEMBER TABLE
CREATE TABLE User (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        provider VARCHAR(255),
                        provider_id VARCHAR(255),
                        email VARCHAR(255),
                        nickname VARCHAR(255),
                        birth VARCHAR(255),
                        gender VARCHAR(255),
                        url VARCHAR(255),
                        createdAt VARCHAR(255),
                        schoolName VARCHAR(255)
);

-- STORE TABLE
CREATE TABLE store (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       kakao_place_id VARCHAR(255) NOT NULL UNIQUE,
                       category_name VARCHAR(255),
                       address VARCHAR(255),
                       road_address VARCHAR(255),
                       name VARCHAR(255),
                       phone VARCHAR(255),
                       kakao_place_url VARCHAR(255),
                       x DOUBLE,
                       y DOUBLE,
                       vote_count INT DEFAULT 0
);

-- FCM_TOKEN TABLE
CREATE TABLE fcm_token (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fcm_token VARCHAR(255),
                        member_id BIGINT UNIQUE,
                        CONSTRAINT fk_fcm_member FOREIGN KEY (member_id) REFERENCES member(id)
);
