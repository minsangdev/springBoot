package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // Rest api용 컨트롤러! 데이터(JSON 형식)를 반환
public class ArticleApiController {

  @Autowired // DI
  private final ArticleRepository articleRepository;

  public ArticleApiController(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  // GET
  @GetMapping("/api/articles")
  public List<Article> index(){
    return articleRepository.findAll();
  }

  @GetMapping("/api/articles/{id}")
  public Article index(@PathVariable Long id){
    return articleRepository.findById(id).orElse(null);
  }

  // POST
  @PostMapping("/api/articles")
  public Article create(@RequestBody ArticleForm dto){
    Article article = dto.toEntity();
    return articleRepository.save(article);

  }

  // PATCH
  @PatchMapping("/api/articles/{id}")
  public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto){
    // 1. 수정용 엔티티 생성
    Article article = dto.toEntity();
    log.info("id: {}, article: {}", id, article.toString());

    // 2. 대상 엔티티를 조회
    Article target = articleRepository.findById(id).orElse(null);

    // 3. 잘못된 요청 처리(대상이 없거나, id가 다른 경우)
    if (target == null || !id.equals(article.getId())){
      // 400, 잘못된 요청 응답!
      log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      // Article로 return 하는게 아니라 ResponseEntity로 담아야함 -> 상태코드를 실어서 보낼 수 있음
    }

    // 4. 올바른 요청 -> 업데이트 및 정상 응답(200)
    target.patch(article);
    // Article updated = articleRepository.save(article); -> 만일 전체가 아닌 몇 개만 보내면 없는 데이터가 null로 날아감
    Article updated = articleRepository.save(target);
    return ResponseEntity.status(HttpStatus.OK).body(updated);
  }

  // DELETE
  @DeleteMapping("api/articles/{id}")
  public ResponseEntity<Article> delte(@PathVariable Long id){
    // 대상 찾기
    Article target = articleRepository.findById(id).orElse(null);

    // 잘못된 요청 처리
    if (target == null) {
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // 대상 삭제
    articleRepository.delete(target);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

}
