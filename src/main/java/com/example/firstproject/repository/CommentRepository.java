package com.example.firstproject.repository;

import com.example.firstproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JpaRepository 는 CrudRepository 인터페이스를 상속하며, 추가적으로 JPA 에 특화된 기능을 제공
// PagingAndSortingRepository 는 Spring Data JPA 에서 제공하는 인터페이스 중 하나
// 이 인터페이스는 CrudRepository 를 상속하며, 추가적으로 페이징과 정렬 기능을 제공
// PagingAndSortingRepository 는 데이터베이스의 데이터를 페이징하여 가져오거나 정렬된 순서로 조회하는 데 사용됨.
// 이를 통해 대량의 데이터를 효율적으로 처리할 수 있음
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 모든 댓글 조회
    // article_id가 주어진 articleId와 일치하는 모든 댓글을 조회하는 쿼리
    // articleId는 바인딩된 파라미터로, findByArticleId 메서드의 articleId 매개변수와 연결
    // 결과적으로 findByArticleId 메서드는 주어진 articleId와 일치하는 모든 댓글을 리스트로 반환
    @Query(value =
            "SELECT * " +
            "FROM comment " +
            "WHERE article_id = :articleId",
            nativeQuery = true)
    List<Comment> findByArticleId(Long articleId);

    // 특정 닉네임의 모든 댓글 조회
    List<Comment> findByNickname(String nickname);
}
