package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {

    private Long id; // id 필드 추가!!
    private String title;
    private String content;

    // Article 객체를 생성하고 반환
    public Article toEntity() {
        // 생성자를 통해 id, title, content 값을 전달하여 Article 객체를 생성
        return new Article(id, title, content);
    }
}
