package com.kkiri_trip.back.domain.jpa.feed.entity;

import lombok.Getter;

@Getter
public enum TripStyleType {
    REST("휴식"),
    EXPERIENCE("체험"),
    ACTIVITY("액티비티"),
    SHOPPING("쇼핑"),
    KNOWLEDGE("견문넓히기"),
    GOURMET("식도락"),
    EMOTIONAL("감성투어"),
    COST_EFFECTIVE("가성비"),
    FLEX("플랙스"),
    PLANNER("꼼꼼한계획"),
    SPONTANEOUS("즉흥"),
    NATURE_FRIENDLY("자연친화"),
    RELAXED("여유"),
    PHOTO_SPOT("인생샷필수"),
    HOT_PLACE("핫플"),
    WAITING_OK("웨이팅가능"),
    RANDOM_FOOD("근처아무식당");

    private final String displayName;

    TripStyleType(String displayName) {
        this.displayName = displayName;
    }
}
