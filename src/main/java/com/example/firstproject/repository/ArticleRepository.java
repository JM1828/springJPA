package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// 관리대상 엔티티, 관리대상 엔티티에서 대표값의 타입
// CrudRepository 는 Spring Data JPA 에서 제공하는 인터페이스로,
// CRUD(create, read, update, delete) 작업을 위한 메서드를 제공
// 이 인터페이스를 상속받으면 Article 엔티티에 대한 CRUD 작업을 수행할 수 있음
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // findAll() 메서드가 재정의되어 있음
    // 이 메서드는 모든 Article 객체를 반환하는 메서드
    @Override
    ArrayList<Article> findAll();
}
