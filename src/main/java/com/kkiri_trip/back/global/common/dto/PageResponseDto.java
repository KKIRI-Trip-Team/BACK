package com.kkiri_trip.back.global.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private final List<T> content;
    private final PageInfo pageInfo;

    @Getter
    public static class PageInfo {
        private final boolean hasNextPage;
        private final boolean hasPrevPage;
        private final int totalPages;
        private final long totalElements;
        private final int currentPage;
        private final int size;

        public PageInfo(Page<?> page) {
            this.hasNextPage = page.hasNext();
            this.hasPrevPage = page.hasPrevious();
            this.totalPages = page.getTotalElements() != 0 ? page.getTotalPages() : 1;
            this.totalElements = page.getTotalElements();
            this.currentPage = page.getTotalElements() != 0 ? page.getNumber() + 1 : 1;
            this.size = page.getSize();
        }
    }

    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.pageInfo = new PageInfo(page);
    }

    public static <T> PageResponseDto<T> empty(){
        return new PageResponseDto<>(Page.empty());
    }
}
