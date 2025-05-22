INSERT INTO users (email, password, created_at, updated_at) VALUES
('test1@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test2@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test3@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test4@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test5@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test6@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test7@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('test8@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_profile (nickname, profile_url, user_id, created_at, updated_at) VALUES
('tester1', 'https://example.com/default.jpg', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester2', 'https://example.com/default.jpg', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester3', 'https://example.com/default.jpg', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester4', 'https://example.com/default.jpg', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester5', 'https://example.com/default.jpg', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester6', 'https://example.com/default.jpg', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester7', 'https://example.com/default.jpg', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tester8', 'https://example.com/default.jpg', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
INSERT INTO trip_style (id, name) VALUES
(1, '휴식'),
(2, '액티비티'),
(3, '맛집'),
(4, '문화');

-- feed
INSERT INTO feed (title, content, region, period, gender, age_group, cost, created_at, updated_at) VALUES
('제목1', '내용1', 'SEOUL', 'ONE_NIGHT', 'ANY', 'TWENTIES', 100000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목2', '내용2', 'BUSAN', 'TWO_NIGHT', 'FEMALE', 'THIRTIES', 200000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목3', '내용3', 'DAEGU', 'DAY_TRIP', 'MALE', 'ANY', 150000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('제목4', '내용4', 'GANGWON', 'OVER_SEVEN', 'ANY', 'FORTIES', 80000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- feed_trip_style
-- feed 1 → '휴식', '맛집'
INSERT INTO feed_trip_style (feed_id, trip_style_id) VALUES (1, 1), (1, 3);

-- feed 2 → '액티비티'
INSERT INTO feed_trip_style (feed_id, trip_style_id) VALUES (2, 2);

-- feed 3 → '문화', '맛집'
INSERT INTO feed_trip_style (feed_id, trip_style_id) VALUES (3, 4), (3, 3);

-- feed 4 → '휴식', '액티비티', '문화'
INSERT INTO feed_trip_style (feed_id, trip_style_id) VALUES (4, 1), (4, 2), (4, 4);


INSERT INTO feed_user (feed_id, user_id, status, is_host, created_at, updated_at) VALUES
(1, 1, 'PENDING', TRUE, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, status, is_host, created_at, updated_at) VALUES
(2, 2, 'PENDING', FALSE, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, status, is_host, created_at, updated_at) VALUES
(3, 3, 'APPROVED', TRUE, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, status, is_host, created_at, updated_at) VALUES
(4, 4, 'REJECTED', FALSE, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);