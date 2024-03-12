package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // RestAPI 용 컨트롤러! 데이터(JSON)를 반환
public class ArticleApiController {

    @Autowired // DI, 생성 객체를 가져와 연결!
    private ArticleService articleService;

    // Article 객체의 목록이 반환되어 클라이언트에게 전달
    // GET
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleService.index();
    }

    // 해당 id 값을 가진 Article 객체가 반환되어 클라이언트에게 전달
    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id) {
        // 특정 id 값을 가진 Article 을 조회하여 반환
        return articleService.show(id);
    }

    // POST
    @PostMapping("/api/articles")
    // @RequestBody 을 사용하여 요청의 본문에 있는 JSON 데이터를 ArticleForm 객체로 변환하여 매개변수 dto 로 받음
    // 이 dto 객체에는 새로운 Article 을 생성하는데 필요한 정보가 포함되어 있음
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto) {
        // ArticleForm 객체를 전달받아 새로운 Article 객체를 생성하는 메서드
        // 이를 통해 새로운 Article 객체가 생성되고, 생성된 객체를 반환
        Article created = articleService.create(dto);
        return (created != null) ?
                // HTTP 상태 코드 200과 함께 생성된 Article 객체를 응답
                ResponseEntity.status(HttpStatus.OK).body(created):
                // 생성된 객체가 없을 경우 HTTP 상태 코드 400을 응답
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // PATCH
    // update 메서드를 호출하고, 업데이트 결과에 따라 적절한 HTTP 응답을 반환
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,
                                          @RequestBody ArticleForm dto) {
        // update 메서드를 호출하여 id와 dto 를 기반으로 Article 객체를 업데이트
        Article updated = articleService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    // delete 메서드는 id를 경로 변수로 받고, articleService.delete(id)를 호출하여 해당 id에 해당하는 Article 객체를 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Article deleted = articleService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 트랜잭션 -> 실패 -> 롤백!
    // 클라이언트가 ArticleForm 객체들을 전송하고,
    // 이를 사용하여 Article 객체들을 생성하고 리스트로 반환하는 트랜잭션 처리를 수행
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos) {
        // ArticleForm 객체들을 사용하여 Article 객체들을 생성하고, 생성된 Article 객체들의 리스트를 반환
        // articleService.createArticles(dtos) 메서드에서 예외가 발생하거나 트랜잭션 처리가 실패할 경우, 롤백되어 이전 상태로 복구될 것임
        List<Article> createdList = articleService.createArticles(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
