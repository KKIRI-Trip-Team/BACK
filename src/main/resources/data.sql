-- users
INSERT INTO users (email, password, created_at, updated_at) VALUES
('test1@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test2@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test3@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test4@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test5@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test6@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test7@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test8@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test9@example.com', '$2a$12$XexvOtb0a97vzNDCHbY8cuI56ERpFbbqcJtMUZ.ZwMwyZmNX.lrqG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- user_profile
INSERT INTO user_profile (nickname, profile_url, user_id, created_at, updated_at) VALUES
('tester1', 'https://example.com/default.jpg', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester2', 'https://example.com/default.jpg', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester3', 'https://example.com/default.jpg', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester4', 'https://example.com/default.jpg', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester5', 'https://example.com/default.jpg', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester6', 'https://example.com/default.jpg', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester7', 'https://example.com/default.jpg', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester8', 'https://example.com/default.jpg', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester9', 'https://example.com/default.jpg', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- pass_info
INSERT INTO pass_info (name, mobile_number, gender, user_id, created_at, updated_at) VALUES
('테스터1', '01000000001', 'M', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터2', '01000000002', 'M', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터3', '01000000003', 'M', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터4', '01000000004', 'M', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터5', '01000000005', 'M', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터6', '01000000006', 'F', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터7', '01000000007', 'F', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('테스터8', '01000000008', 'F', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- trip_style
INSERT INTO trip_style (type) VALUES ('REST');                 -- 휴식
INSERT INTO trip_style (type) VALUES ('EXPERIENCE');           -- 체험
INSERT INTO trip_style (type) VALUES ('ACTIVITY');             -- 액티비티
INSERT INTO trip_style (type) VALUES ('SHOPPING');             -- 쇼핑
INSERT INTO trip_style (type) VALUES ('KNOWLEDGE');            -- 견문넓히기 → 대체된 값
INSERT INTO trip_style (type) VALUES ('GOURMET');              -- 식도락
INSERT INTO trip_style (type) VALUES ('EMOTIONAL_TOUR');       -- 감성투어
INSERT INTO trip_style (type) VALUES ('COST_EFFECTIVE');       -- 가성비
INSERT INTO trip_style (type) VALUES ('FLEX');                 -- 플랙스
INSERT INTO trip_style (type) VALUES ('METICULOUS_PLANNING');  -- 꼼꼼한계획
INSERT INTO trip_style (type) VALUES ('SPONTANEOUS');          -- 즉흥
INSERT INTO trip_style (type) VALUES ('NATURE_FRIENDLY');      -- 자연친화
INSERT INTO trip_style (type) VALUES ('RELAXED');              -- 여유
INSERT INTO trip_style (type) VALUES ('MUST_TAKE_PHOTO');      -- 인생샷필수
INSERT INTO trip_style (type) VALUES ('HOT_PLACE');            -- 핫플
INSERT INTO trip_style (type) VALUES ('WAITING_OK');           -- 웨이팅가능
INSERT INTO trip_style (type) VALUES ('ANY_NEARBY_RESTAURANT');-- 근처아무식당


-- feed
INSERT INTO feed (title, content, region, period, gender, age_group, cost, created_at, updated_at) VALUES
('제목1', '내용1', 'SEOUL', 'ONE_NIGHT', 'ANY', 'TWENTIES', 100000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목2', '내용2', 'BUSAN', 'TWO_NIGHT', 'FEMALE', 'THIRTIES', 200000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목3', '내용3', 'DAEGU', 'DAY_TRIP', 'MALE', 'ANY', 150000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목4', '내용4', 'GANGWON', 'OVER_SEVEN', 'ANY', 'FORTIES', 80000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_image (feed_id, url) VALUES
(1, 'https://example.com/default.jpg'),
(1, 'https://example.com/default.jpg'),
(1, 'https://example.com/default.jpg'),
(2, 'https://example.com/default.jpg'),
(2, 'https://example.com/default.jpg'),
(2, 'https://example.com/default.jpg'),
(3, 'https://example.com/default.jpg'),
(3, 'https://example.com/default.jpg'),
(3, 'https://example.com/default.jpg'),
(4, 'https://example.com/default.jpg'),
(4, 'https://example.com/default.jpg'),
(4, 'https://example.com/default.jpg');
-- feed_trip_style
INSERT INTO feed_trip_style (feed_id, trip_style_id) VALUES
(1, 1), (1, 3),       -- feed 1 → 'REST', 'GOURMET'
(2, 2),               -- feed 2 → 'ACTIVITY'
(3, 4), (3, 3),       -- feed 3 → 'KNOWLEDGE', 'GOURMET'  (원래 'CULTURE' → 'KNOWLEDGE' 대체)
(4, 1), (4, 2), (4, 4); -- feed 4 → 'REST', 'ACTIVITY', 'KNOWLEDGE'

-- feed_user
INSERT INTO feed_user (feed_id, user_id, status, is_host, created_at, updated_at) VALUES
(1, 1, 'PENDING', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'PENDING', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 'APPROVED', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 'REJECTED', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
