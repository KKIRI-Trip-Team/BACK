package com.kkiri_trip.back.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyPostDataLoader implements CommandLineRunner {
    private final PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() > 0) return;

        List<Post> posts = new ArrayList<>();
        int total = 100_000;

        for (int i = 1; i <= total; i++) {
            Post post = new Post();
            post.setTitle("스프링 성능 테스트 게시글 " + i);
            post.setContent("이것은 검색 기능 테스트용 내용입니다. 키워드" + (i % 100));
            post.setWriter("작성자" + (i % 10));
            post.setCreatedAt(LocalDateTime.now().minusDays((long) (Math.random() * 365)));

            posts.add(post);
            if (i % 1000 == 0) {
                postRepository.saveAll(posts);
                posts.clear();
            }
        }

        postRepository.saveAll(posts);
        System.out.println("더미 데이터 생성 완료!");
    }
}
