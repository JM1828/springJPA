package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest // 해당 클래스는 스프링부트와 연동되어 테스팅된다.
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    // 예상 값과 실제 값이 동일한지를 확인하고, 동일하지 않을 경우에는 테스트를 실패로 처리
    @Test
    void index() {
        // 예상
        Article a = new Article(1L, "가가가가", "1111");
        Article b = new Article(2L, "나나나나", "2222");
        Article c = new Article(3L, "다다다다", "3333");
        // 주어진 인자들을 가지고 새로운 List 를 생성하는 메서드
        // 여기서는 a, b, c라는 세 개의 Article 객체를 인자로 전달하여 새로운 List<Article>를 생성하고 있음
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a, b, c));

        // 실제
        List<Article> articles = articleService.index();

        // 비교
        // expected 와 articles 두 개의 리스트를 문자열로 변환한 후, 이 두 문자열을 비교하여 검증하는 역할을 수행
        assertEquals(expected.toString(), articles.toString());
    }

    // articleService.show(id)를 호출하여 반환된 Article 객체와 예상한 expected 객체를 비교하여 검증
    @Test
    void show_성공____존재하는_id_입력() {
        // 예상
        Long id = 1L;
        // expected 변수에 id 값을 사용하여 새로운 Article 객체를 생성
        Article expected = new Article(id, "가가가가", "1111");

        // 실제
        // id 값을 사용하여 Article 객체를 가져옴
        Article article = articleService.show(id);

        // 비교
        // expected 객체와 article 객체의 문자열 표현을 비교, 이를 통해 두 객체가 동일한지 검증
        assertEquals(expected.toString(), article.toString());
    }

    // 존재하지 않는 id 값을 입력하여 articleService.show(id)를 호출하고, 반환된 Article 객체와 예상한 expected 객체를 비교하여 검증
    @Test
    void show_실패____존재하지_않는_id_입력() {
        // 예상
        // id 변수에 -1L 값을 할당합니다. 이는 존재하지 않는 id 값을 나타냄
        Long id = -1L;
        // 존재하지 않는 id에 대한 Article 객체는 null 이기를 예상
        Article expected = null;

        // 실제
        // id 값을 사용하여 Article 객체를 가져옴
        // 이 경우, 존재하지 않는 id에 대한 Article 객체는 null 이 반환될 것임
        Article article = articleService.show(id);

        // 비교
        // expected 객체와 article 객체를 비교, 이를 통해 두 객체가 동일한지 검증
        // 여기서는 null 과 null 을 비교하므로, 두 객체가 동일하면 테스트는 통과
        assertEquals(expected, article);
    }

    @Test
    @Transactional
    void create_성공____title과_content만_있는_dto_입력() {
        // 예상
        String title = "라라라라";
        String content = "4444";
        // 이 객체는 title 과 content 만을 가지고 있음
        ArticleForm dto = new ArticleForm(null, title, content);
        // title, content, 그리고 새로운 Article 객체에 할당될 id 값을 사용하여 새로운 Article 객체를 생성
        Article expected = new Article(4L, title, content);

        // 실제
        // dto 를 사용하여 Article 객체를 생성
        Article article = articleService.create(dto);

        // 비교
        // expected 객체와 article 객체의 문자열 표현을 비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional
    void create_실패____id가_포함된_dto_입력() {
        // 예상
        String title = "라라라라";
        String content = "4444";
        // id 값은 일반적으로 데이터베이스에서 자동으로 생성되는 값.
        ArticleForm dto = new ArticleForm(4L, title, content);
        // expected 변수에는 테스트의 예상 결과를 담기 위해 null 로 설정
        Article expected = null;

        // 실제
        // id가 포함된 dto 를 입력으로 전달했을 때 create 메서드가 실패하고 null 을 반환
        Article article = articleService.create(dto);

        // 비교
        assertEquals(expected, article);
    }

    @Test
    @Transactional
    void update_성공____존재하는_id와_title_content가_있는_dto_입력() {
        // 예상
        Long id = 1L;
        String title = "구준모";
        String content = "1234";
        ArticleForm dto = new ArticleForm(id, title, content);
        Article expected = new Article(id, title, content);

        // 실제
        Article article = articleService.update(id, dto);

        // 비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional
    void update_성공____존재하는_id와_title만_있는_dto_입력() {
        //예상
        Long id = 1L;
        String title = "이병훈";
        ArticleForm dto = new ArticleForm(id, title, null);
        Article expected = new Article(id, title, null);

        // 실제
        Article article = articleService.update(id, dto);

        // 비교
        assertEquals(expected, article);
    }

    @Test
    @Transactional
    void update_실패____존재하지_않는_id의_dto_입력() {
        // 예상
        Long id = -5L;

        ArticleForm dto = new ArticleForm(id, null, null);
        // expected 변수에는 테스트의 예상 결과를 담기 위해 null 로 설정
        Article expected = null;

        // 실제
        Article article = articleService.update(id, dto);

        // 비교
        assertEquals(expected, article);
    }

//    @Test
//    @Transactional
//    void update_실패____id만_있는_dto_입력() {
//        // 예상
//        Long id = 1L;
//        String title = "가가가가";
//        String content = "1111";
//
//        Article expected = new Article(id, title, content);
//
//
//        ArticleForm dto = new ArticleForm(id, null, null);
//
//        // 실제
//        Article article = articleService.update(id, dto);
//
//        // 비교
//        assertNull(article);
//    }

    @Test
    @Transactional
    void delete_성공____존재하는_id_입력() {
        // 예상
        Long id = 1L;
        String title = "가가가가";
        String content = "1111";
        Article expected = new Article(id, title, content);

        // 실제
        Article article = articleService.delete(id);

        // 비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional
    void delete_실패____존재하지_않는_id_입력() {
        // 예상
        Long id = -4L;
        Article expected = null;

        // 실제
        Article article = articleService.delete(id);

        // 비교
        assertEquals(expected, article);
    }
}