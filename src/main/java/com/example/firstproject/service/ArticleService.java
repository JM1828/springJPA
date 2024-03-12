package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service // 서비스 선언! (서비스 객체를 스프링부트에 생성)
public class ArticleService {

    @Autowired // DI (외부에서 가져온다는 뜻)
    private ArticleRepository articleRepository;

    // Article 객체의 목록을 조회하는 기능을 수행
    public List<Article> index() {
        // 데이터베이스에 저장된 모든 Article 객체를 가져와서 목록으로 반환
        return articleRepository.findAll();
    }

    // 특정 id 값을 가진 Article 객체를 조회하여 반환
    public Article show(Long id) {
        // 데이터베이스에서 해당 id 값을 가진 Article 을 찾으면 해당 객체를 반환하고, 찾지 못할 경우 null 을 반환
        return articleRepository.findById(id).orElse(null);
    }

    // ArticleForm 객체를 기반으로 Article 객체를 생성하고 저장한 후, 저장된 객체를 반환
    public Article create(ArticleForm dto) {
        // ArticleForm 객체인 dto 를 기반으로 Article 객체를 생성
        Article article = dto.toEntity();
        // article 객체의 ID를 확인, ID가 이미 존재한다면(즉, 이미 저장된 Article 객체인 경우) null 을 반환
        if (article.getId() != null) {
            return null;
        }
        // ID가 존재하지 않는 경우, 새로운 Article 객체가 데이터베이스에 저장되고, 저장된 객체가 반환
        return articleRepository.save(article);
    }

    // 주어진 ID에 해당하는 기존 Article 객체를 찾아서 수정용 Article 객체로 업데이트
    public Article update(Long id, ArticleForm dto) {
        // 1. 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());

        // 2. 대상 엔티티 찾기
        // 주어진 ID에 해당하는 기존 Article 객체를 찾음, 만약 해당 ID에 해당하는 Article 객체가 없다면 null 을 반환
        Article target = articleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리(대상이 없거나, id가 다른경우)
        // 만약 target 이 null 이거나 id와 article.getId()가 다른 경우, 잘못된 요청으로 간주하고 null 을 반환
        if (target == null || id != article.getId()) {
            log.info("잘못된 요청! id: {}, article: {}", id, article);
            return null;
        }

        // 4. 업데이트
        // target 객체를 주어진 article 객체로 업데이트
        target.patch(article);
        // 업데이트된 target 객체를 저장
        // 이를 통해 업데이트된 Article 객체가 데이터베이스에 저장되고, 저장된 객체가 반환
        Article updated = articleRepository.save(target);
        return updated;
    }

    // id를 인자로 받아서 해당 id에 해당하는 Article 객체를 삭제하고, 삭제된 Article 객체를 반환
    public Article delete(Long id) {
        // 대상 엔티티 찾기
        // articleRepository 를 사용하여 id에 해당하는 Article 객체를 찾는다.
        // findById(id) 메서드를 호출하고, 반환된 객체를 target 변수에 할당
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리(대상이 없는 경우)
        // 만약 target 이 null 인 경우, 즉 대상 Article 객체가 없는 경우에는 null 을 반환
        if (target == null) {
            return null;
        }

        // 대상 삭제 후 응답 반환
        // target 에 해당하는 Article 객체를 삭제
        articleRepository.delete(target);
        // 삭제된 Article 객체를 반환하기 위해 target 을 반환
        return target;
    }

    // ArticleForm 객체들을 Article 엔티티로 변환하여 DB에 저장하고,
    // 저장된 Article 객체들의 리스트를 반환하는 createArticles 메서드를 트랜잭션 내에서 실행하는 구현을 보여줌
    @Transactional // 해당 메소드를 트랜잭션으로 묶는다!
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dto 묶음을 entity 묶음으로 변환
        // 주어진 ArticleForm 객체들의 리스트를 stream() 메서드를 사용하여 스트림으로 변환
        // stream()은 Java 8부터 추가된 기능으로, 컬렉션(Collection)이나 배열과 같은 데이터 요소들의 시퀀스를 처리하는데 사용되는 기능
        List<Article> articleList = dtos.stream()
                // map() 메서드를 사용하여 각 ArticleForm 객체를 Article 엔티티로 변환
                // map() 메서드는 스트림의 각 요소를 하나씩 가져와서 지정된 함수를 적용한 후, 그 결과를 새로운 요소로 매핑
                // 람다 표현식 dto -> dto.toEntity()를 사용하여 dto 객체를 toEntity() 메서드를 호출하여 Article 엔티티로 변환
                // 람다 표현식은 익명 함수를 간결하게 표현하는 방법, (매개변수) -> {실행문}
                .map(dto -> dto.toEntity())
                // collect(Collectors.toList())는 Java 의 Stream API 에서 제공하는 메서드 중 하나
                // 스트림(Stream)에서 요소들을 수집하여 리스트(List)로 변환하는 작업을 수행
                .collect(Collectors.toList());

        // 2. entity 묶음을 DB로 저장
        // 변환된 Article 객체들의 리스트를 stream() 메서드를 사용하여 스트림으로 변환
        articleList.stream()
                // forEach() 메서드를 사용하여 각 Article 객체를 articleRepository.save(article)을 호출하여 DB에 저장
                .forEach(article -> articleRepository.save(article));

        // 3. 강제 예외 발생
        // -1L에 해당하는 Article 객체를 찾음, 만약 해당 객체가 없다면 IllegalArgumentException 을 발생시킴
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패!")
        );

        // 4. 결과값 반환
        return articleList;
    }
}
