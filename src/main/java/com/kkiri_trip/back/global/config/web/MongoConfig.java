package com.kkiri_trip.back.global.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.kkiri_trip.back.domain.chat")  // MongoDB 리포지토리 패키지
@EnableJpaRepositories(basePackages = "com.kkiri_trip.back.domain")  // JPA 리포지토리 패키지 (ChatMessageRepository 제외)
public class MongoConfig {
}
