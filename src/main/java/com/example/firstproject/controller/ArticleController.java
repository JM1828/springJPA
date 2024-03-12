package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j // 로깅을 위한 골뱅이(애노테이션) (서버에서 일어나는 일들을 실시간으로 기록)
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    // post 방식으로 /articles/create 로 매핑
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        // form 객체를 문자열로 변환하여 로깅하는 기능을 수행
        log.info(form.toString()); // 로깅기능으로 대체 (서버에서 일어나는 일들을 실시간으로 기록)

        // 1. Dto 를 변환! Entity!
        // form.toEntity() 메서드를 호출하여 form 객체를 Article 엔티티로 변환
        // form 객체의 id, title, content 값을 사용하여 새로운 Article 객체를 생성하고 반환
        // 이렇게 생성된 Article 객체는 article 변수에 할당
        Article article = form.toEntity();
        log.info(article.toString());

        // 2. Repository 에게 Entity 를 DB 안에 저장하게 함!
        // CrudRepository 인터페이스에서 상속받은 메서드로, 객체를 저장하고 저장된 객체를 반환
        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        // 3. /articles/{id} 로 리다이렉트
        // saved.getId()를 사용하여 /articles/{id} 형식의 URL 을 생성
        // 여기서 {id}는 Article 객체의 실제 ID 값으로 대체됨
        return "redirect:/articles/" + saved.getId();
    }

    // 게시글 상세보기
    @GetMapping("/articles/{id}")
    // URL Path 로 부터 입력이된다 라는걸 의미 (@PathVariable)
    public String show(@PathVariable Long id, Model model) {
        // 잘받아와 지는지 log.info 로 확인
        log.info("id = " + id);

        // 1. id로 데이터를 가져옴!
        // ArticleRepository 를 사용하여 전달받은 id 값을 가지고 해당 ID에 해당하는 게시글을 찾아옴
        // 만약 해당 ID의 게시글이 존재하지 않으면 null을 반환
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 가져온 데이터를 모델에 등록!
        // Model 객체에 articles 라는 이름으로 articleEntity 를 등록하는 부분
        // 이를 통해 articles 라는 이름으로 게시글 정보를 뷰로 전달할 수 있음
        model.addAttribute("articles", articleEntity);

        // 3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    // 해당 메서드는 모든 게시글을 가져와서 뷰로 전달하는 기능을 구현
    @GetMapping("/articles")
    public String index(Model model) {
        // 1. 모든 Article 을 가져온다!
        // ArticleRepository 를 사용하여 모든 게시글을 가져오는 메서드
        // 이를 통해 데이터베이스에 저장된 모든 게시글을 articleEntityList 에 저장
        List<Article> articleEntityList = articleRepository.findAll();

        // 2. 가져온 Article 묶음을 뷰로 전달!
        // Model 객체에 articleList 라는 이름으로 articleEntityList 를 등록하는 부분
        // 이를 통해 articleList 라는 이름으로 게시글 목록을 뷰로 전달할 수 있음.
        model.addAttribute("articleList" ,articleEntityList);

        // 3. 뷰 페이지를 설정!
        // 보여줄 페이지를 설정하는 부분
        return "articles/index"; // articles/index.mustache
    }

    // 해당 메서드는 특정 게시글을 수정하기 위해 수정할 데이터를 가져와서 뷰로 전달하는 기능을 구현
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 수정할 데이터를 가져오기!
        // 주어진 id에 해당하는 게시글을 찾는 메서드
        // findById(id)는 Optional 타입으로 반환되며, 게시글이 존재하지 않을 경우 null 을 반환
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록!
        // Model 객체에 articles 라는 이름으로 articleEntity 를 등록하는 부분
        // 이를 통해 articles 라는 이름으로 수정할 게시글 데이터를 뷰로 전달
        model.addAttribute("articles", articleEntity);

        // 뷰 페이지 설정
        // 보여줄 페이지를 설정하는 부분
        return "articles/edit";
    }

    // 해당 메서드는 게시글을 수정한 후 수정 결과 페이지로 리다이렉트하는 기능을 구현
    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());

        // 1. DTO 를 엔티티로 변환한다!
        // Article 엔티티로 변환하는 메서드를 호출하는 부분
        // 이를 통해 DTO(데이터 전송 객체)를 엔티티로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 2. 엔티티를 DB로 저장한다!
        // 2-1. DB 에서 기존 데이터를 가져온다!
        // ArticleRepository 를 사용하여 주어진 articleEntity 의 id에 해당하는 기존 게시글을 찾는 메서드
        // findById(articleEntity.getId())는 Optional 타입으로 반환되며, 게시글이 존재하지 않을 경우 null 을 반환
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        // 2-2. 기존 데이터에 값을 갱신한다!
        // 기존 게시글이 존재하는지 확인하는 조건문
        // 기존 게시글이 존재할 경우, articleRepository.save(articleEntity)를 통해 엔티티를 DB에 저장하여 값을 갱신
        if (target != null) {
            articleRepository.save(articleEntity); // 엔티티가 DB로 갱신!
        }

        // 3. 수정 결과 페이지로 리다이렉트 한다!
        // 수정된 게시글의 아이디를 포함한 URL 로 리다이렉트하여 수정 결과 페이지를 보여줌
        return "redirect:/articles/" + articleEntity.getId();
    }

    // 해당 메서드는 게시글을 삭제한 후 결과 페이지로 리다이렉트하는 기능을 구현
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다!!");

        // 1. 삭제 대상을 가져온다!
        // ArticleRepository 를 사용하여 주어진 id에 해당하는 게시글을 찾는 메서드
        // findById(id)는 Optional 타입으로 반환되며, 게시글이 존재하지 않을 경우 null 을 반환
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 그 대상을 삭제한다!
        // 삭제 대상 게시글이 존재하는지 확인하는 조건문
        if (target != null) {
            // 제 대상 게시글이 존재할 경우, articleRepository.delete(target)를 통해 해당 게시글을 삭제
            articleRepository.delete(target);
            // 플래시 속성은 리다이렉트된 다음 요청에서만 유효한 속성으로, 일회성 메시지나 데이터 전달에 사용
            // "msg"라는 이름으로 "삭제가 완료되었습니다."라는 메시지를 추가
            rttr.addFlashAttribute("msg", "삭제가 완료 되었습니다.");
        }

        // 3. 결과 페이지로 리다이렉트 한다!
        // 게시글을 삭제한 후 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}
