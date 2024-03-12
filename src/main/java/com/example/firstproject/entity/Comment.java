package com.example.firstproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JoinColumn(name = "article_id")를 사용하면 @ManyToOne 관계에서 현재 엔티티의 외래 키 컬럼을 "article_id"라는 이름으로 매핑할 수 있다.

    // 다른 엔티티와의 다대일 관계를 매핑
    // @ManyToOne 애노테이션을 사용한 필드는 외래 키를 가진다, 이 외래 키는 매핑된 엔티티의 기본 키(Primary Key)를 참조
    // 즉, 하나의 Article 에 대해 여러 개의 댓글이 연결
    @ManyToOne
    // @ManyToOne 애노테이션과 함께 사용되는 JPA 애너테이션, 이 애너테이션은 외래 키 컬럼의 매핑 정보를 지정하는 데 사용
    // @JoinColumn 애노테이션의 name 속성은 외래 키 컬럼의 이름을 지정
    // "article_id"라는 이름을 가진 외래 키 컬럼을 매핑, 이는 현재 엔티티의 외래 키 컬럼이 "article_id"라는 이름을 가지도록 설정하는 것을 의미
    @JoinColumn(name = "article_id") // "articleid" 컬럼에 Article의 대표값을 저장!
    private Article article;

    @Column
    private String nickname;

    @Column
    private String body;
}
