package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
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

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    // post 방식으로 /articles/create 로 매핑
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        // 1. Dto 를 변환! Entity!
        Article article = form.toEntity();

        // 2. Repository 에게 Entity 를 DB 안에 저장하게 함!
        Article saved = articleRepository.save(article);

        // 3. /articles/{id} 로 리다이렉트
        return "redirect:/articles/" + saved.getId();
    }

    // 게시글 상세보기
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        // 1. id로 데이터를 가져옴!
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

        // 2. 가져온 데이터를 모델에 등록!
        model.addAttribute("articles", articleEntity);
        model.addAttribute("commentDtos", commentDtos);

        // 3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    // 해당 메서드는 모든 게시글을 가져와서 뷰로 전달하는 기능을 구현
    @GetMapping("/articles")
    public String index(Model model) {
        // 1. 모든 Article 을 가져온다!
        List<Article> articleEntityList = articleRepository.findAll();

        // 2. 가져온 Article 묶음을 뷰로 전달!
        model.addAttribute("articleList" ,articleEntityList);

        // 3. 뷰 페이지를 설정!
        return "articles/index"; // articles/index.mustache
    }

    // 해당 메서드는 특정 게시글을 수정하기 위해 수정할 데이터를 가져와서 뷰로 전달하는 기능을 구현
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 수정할 데이터를 가져오기!
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록!
        model.addAttribute("articles", articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
    }

    // 해당 메서드는 게시글을 수정한 후 수정 결과 페이지로 리다이렉트하는 기능을 구현
    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        // 1. DTO 를 엔티티로 변환한다!
        Article articleEntity = form.toEntity();

        // 2. 엔티티를 DB로 저장한다!
        // 2-1. DB 에서 기존 데이터를 가져온다!
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        // 2-2. 기존 데이터에 값을 갱신한다!
        if (target != null) {
            articleRepository.save(articleEntity); // 엔티티가 DB로 갱신!
        }

        // 3. 수정 결과 페이지로 리다이렉트 한다!
        return "redirect:/articles/" + articleEntity.getId();
    }

    // 해당 메서드는 게시글을 삭제한 후 결과 페이지로 리다이렉트하는 기능을 구현
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        // 1. 삭제 대상을 가져온다!
        Article target = articleRepository.findById(id).orElse(null);

        // 2. 그 대상을 삭제한다!
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료 되었습니다.");
        }

        // 3. 결과 페이지로 리다이렉트 한다!
        return "redirect:/articles";
    }
}
