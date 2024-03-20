package com.example.firstproject.dto;

import com.example.firstproject.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor // 모든 생성자 자동 완성
@NoArgsConstructor // 디폴트 생성자 자동 완성
@Getter
@ToString
public class CommentDto {
    private Long id; // 댓글의 고유 식별자

    @JsonProperty("article_id") // JSON 데이터에서 해당 필드를 받아올 때 사용
    private Long articleId; // 댓글이 속한 게시글의 식별자
    private String nickname; // 댓글 작성자의 닉네임
    private String body; // 댓글의 내용

    // 엔티티를 DTO 로 변환하는 이유는 주로 데이터 전송이나 데이터 표현의 목적
    // DTO 를 사용하여 엔티티의 필요한 데이터만 전송하거나 특정 형식으로 변환하여 클라이언트에게 전달할 수 있음

    // Comment 객체를 매개변수로 받아서 해당 객체의 정보를 활용하여 CommentDto 객체를 생성
    // Comment 객체를 CommentDto 로 변환하는 작업
    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getNickname(),
                comment.getBody()
        );
    }
}
