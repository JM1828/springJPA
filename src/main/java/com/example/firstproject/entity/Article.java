package com.example.firstproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity // DB가 해당 객체를 인식 가능! (해당 클래스로 테이블을 만든다!)
@AllArgsConstructor // 먜개변수 있는 생성자
@NoArgsConstructor // 디폴트 생성자를 추가!
@ToString
@Getter
public class Article {

    @Id // 데이터베이스에서 대표값을 지정하는 역할, 주민등록번호와 같이 고유한 식별자로 사용될 수 있음
    // 데이터베이스가 id 값을 자동으로 생성하는 전략을 지정
    // 데이터베이스에서 제공하는 자동 증가 기능을 사용하여 id 값을 자동으로 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    // Article 객체를 이용하여 현재 객체의 title 과 content 값을 업데이트함.
    // 이때, 주어진 Article 객체의 title 과 content 필드가 null 이 아닌 경우에만 값을 업데이트함.
    public void patch(Article article) {
        if (article.title != null)
            this.title = article.title;
        if (article.content != null)
            this.content = article.content;
    }
}
