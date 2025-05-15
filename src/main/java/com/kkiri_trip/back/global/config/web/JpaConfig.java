package com.kkiri_trip.back.global.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.kkiri_trip.back.domain.chat.repository.ChatMessageRepository;

@Configuration
@EnableJpaRepositories(basePackages = "com.kkiri_trip.back.domain",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ChatMessageRepository.class))  // JPA 리포지토리 패키지
public class JpaConfig {
}
