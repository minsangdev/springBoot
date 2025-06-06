package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service // 서비스 선언! (서비스 객체를 스프링부트에 생성)
public class ArticleService {
  @Autowired // DI
  private ArticleRepository articleRepository;

  public List<Article> index() {
    return articleRepository.findAll();
  }

  public Article show(Long id) {
    return articleRepository.findById(id).orElse(null);
  }

  public Article create(ArticleForm dto) {
    Article article = dto.toEntity();
    if (article.getId()!=null) {
      return null;
    }
    return articleRepository.save(article);
  }

  public Article update(Long id, ArticleForm dto) {
    // 1: 수정용 엔티티 생성
    Article article = dto.toEntity();
    log.info("id: {}, article: {}",id,article.toString());

    // 2: 대상 엔티티 찾기
    Article target = articleRepository.findById(id).orElse(null);

    // 3: 잘못된 요청 처리(대상이 없거나, id가 다른 경우)
    if (target == null || !id.equals(article.getId())){
      log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
      // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      // 응답에 대한건 웨이터의 역할
      return null;
    }

    // 4: 업데이트
    target.patch(article);
    Article updated = articleRepository.save(target);
    return updated;
  }



  public Article delete(Long id) {
    // 대상 찾기
    Article target = articleRepository.findById(id).orElse(null);

    // 잘못된 요청 처리(대상이 없는 경우)
    if (target == null) {
      return null;
    }
    // 대상 삭제
    articleRepository.delete(target);

    return target;
  }

  @Transactional // 해당 메소드를 트랜잭션으로 묶는다! -> 해당 메소드 수행하다 실패하면 처음으로 롤백
  public List<Article> createArticles(List<ArticleForm> dtos) {
    // dto 묶음을 entity 묶음으로 변환
    List<Article> articleList = dtos.stream()
        .map(dto -> dto.toEntity())
        .collect(Collectors.toList());

    // entity 묶음을 db로 저장
    articleList.stream()
        .forEach(article -> articleRepository.save(article));

    // 강제 예외 발생
    articleRepository.findById(-1L).orElseThrow(
        () -> new IllegalArgumentException("결재 실패!")
    );

    // 결과값 반환
    return articleList;
  }
}
