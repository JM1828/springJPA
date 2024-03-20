package com.example.firstproject.entity;

import com.example.firstproject.dto.CommentDto;
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

    // @ManyToOne 애노테이션을 사용한 필드는 외래 키를 가진다, 이 외래 키는 매핑된 엔티티의 기본 키(Primary Key)를 참조
    // 즉, 하나의 Article 에 대해 여러 개의 댓글이 연결 (다대일 관계)
    @ManyToOne
    // @JoinColumn 애노테이션의 name 속성은 외래 키 컬럼의 이름을 지정
    // "article_id"라는 이름을 가진 외래 키 컬럼을 매핑, 이는 현재 엔티티의 외래 키 컬럼이 "article_id"라는 이름을 가지도록 설정하는 것을 의미
    @JoinColumn(name = "article_id") // "articleid" 컬럼에 Article의 대표값을 저장!
    private Article article;

    @Column
    private String nickname;

    @Column
    private String body;

    // CommentDto 와 Article 객체를 매개변수로 받아서 댓글을 생성하고 반환
    public static Comment createComment(CommentDto dto, Article article) {
        // 예외 발생
        if (dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        if (dto.getArticleId() != article.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못되었습니다.");

        // 엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                dto.getNickname(),
                dto.getBody()
        );
    }

    // CommentDto 객체를 매개변수로 받아서 댓글을 수정
    public void patch(CommentDto dto) {
        // 예외 발생
        if (this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력되었습니다.");

        // 객체를 갱신
        if (dto.getNickname() != null)
            this.nickname = dto.getNickname();

        if (dto.getBody() != null)
            this.body = dto.getBody();
    }
}
