package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    @Autowired // // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
    // CommentService 클래스는 데이터베이스와의 CRUD 작업 등을 처리
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // 클라이언트에게는 DTO 형식으로 데이터를 전달하는 것이 일반적으로 더 유용
    // DTO 는 엔티티와는 다른 데이터 구조를 가지며, 필요한 필드만 포함하여 데이터 전송을 간소화할 수 있음
    // 따라서, stream()과 map()을 사용하여 엔티티를 DTO 로 변환하여 일관된 형식의 데이터를 반환하는 것이 좋다.

    // articleId를 매개변수로 받아서 해당 게시글에 대한 댓글들을 조회하여 CommentDto 의 리스트로 반환
    public List<CommentDto> comments(Long articleId) {
        // 반환
        // articleId에 해당하는 댓글들을 조회, 그 후, 각댓글에 대해 createCommentDto 호출하여 CommentDto 의 리스트로 변환
        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment -> CommentDto.createCommentDto(comment))
                .collect(Collectors.toList());
    }

    // 새로운 댓글을 생성하는 기능을 담당
    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {

        // 게시글 조회 및 예외 발생
        // articleId에 해당하는 게시글을 조회
        Article article = articleRepository.findById(articleId)
                // 만약 조회된 게시글이 없다면 예외 발생
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));

        // 댓글 엔티티 생성
        // dto 와 article 을 기반으로 새로운 댓글 엔티티를 생성
        Comment comment = Comment.createComment(dto, article);

        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        // DTO 로 변경하여 반환
        return CommentDto.createCommentDto(created);
    }

    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 댓글 조회 및 예외 발생
        // id에 해당하는 댓글을 조회
        Comment target =  commentRepository.findById(id)
                // 만약 조회된 게시글이 없다면 예외 발생
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));

        // 댓글 수정
        target.patch(dto);

        // DB로 갱신
        Comment updated = commentRepository.save(target);

        // 댓글 엔티티를 DTO 로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) {
        // 댓글 조회 및 예외 발생
        // id에 해당하는 댓글을 조회
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다."));

        // 댓글 삭제
        commentRepository.delete(target);

        // 삭제 댓글을 DTO 로 반환
        return CommentDto.createCommentDto(target);
    }
}
