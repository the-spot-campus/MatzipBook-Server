-- MEMBER (User) INSERT
INSERT INTO User (provider, provider_id, email, nickname, birth, gender, url, createdAt, schoolName)
VALUES
    ('kakao', 'kakao_123', 'user1@example.com', 'Alice', '2000-01-01', 'F', 'http://img.com/1', '2024-01-01', 'Highschool A'),
    ('google', 'google_456', 'user2@example.com', 'Bob', '1999-12-31', 'M', 'http://img.com/2', '2024-02-01', 'Highschool B'),
    ('naver', 'naver_789', 'user3@example.com', 'Charlie', '2001-06-15', 'M', 'http://img.com/3', '2024-03-01', 'Highschool C');

-- STORE INSERT
INSERT INTO store (kakao_place_id, category_name, address, road_address, name, phone, kakao_place_url, x, y, vote_count)
VALUES
    ('kakao_001', 'Korean', '서울시 강남구 123', '서울시 강남대로 123', '맛집1', '010-1234-5678', 'http://place.kakao.com/1', 127.027, 37.497, 10),
    ('kakao_002', 'Japanese', '서울시 종로구 456', '서울시 종로대로 456', '맛집2', '010-2345-6789', 'http://place.kakao.com/2', 126.978, 37.566, 20),
    ('kakao_003', 'Cafe', '서울시 마포구 789', '서울시 마포대로 789', '맛집3', '010-3456-7890', 'http://place.kakao.com/3', 126.910, 37.550, 5);

-- FCM_TOKEN INSERT
INSERT INTO fcm_token (fcm_token, member_id)
VALUES
    ('token1234', 1),
    ('token5678', 2),
    ('token9012', 3);
