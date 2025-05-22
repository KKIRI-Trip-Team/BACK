package com.kkiri_trip.back.domain.jpa.feed.entity;

import com.kkiri_trip.back.domain.jpa.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TripStyle extends BaseEntity {

    @Column(unique = true)
    private String name; // 감성투어, 식도락, 플렉스 등
}