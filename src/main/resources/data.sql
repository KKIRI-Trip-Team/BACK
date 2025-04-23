INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test1@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터1', 'tester1', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test2@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터2', 'tester2', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test3@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터3', 'tester3', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test4@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터4', 'tester4', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test5@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터5', 'tester5', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test6@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터6', 'tester6', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test7@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터7', 'tester7', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (email, password, name, nickname, mobile_number, gender, created_at, updated_at) VALUES
('test8@example.com', '$2b$12$wGz.CbY9tY3ABmGwRmIH4OM3b5alGer863Wh5oXemyJp4ztPfVO/a', '테스터8', 'tester8', '01000000001', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed (title, content, created_at, updated_at) VALUES
('제목1', '내용1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed (title, content, created_at, updated_at) VALUES
('제목2', '내용2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed (title, content, created_at, updated_at) VALUES
('제목3', '내용3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed (title, content, created_at, updated_at) VALUES
('제목4', '내용4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, created_at, updated_at) VALUES
(1, 1, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, created_at, updated_at) VALUES
(2, 2, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, created_at, updated_at) VALUES
(3, 3, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO feed_user (feed_id, user_id, created_at, updated_at) VALUES
(4, 4, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);