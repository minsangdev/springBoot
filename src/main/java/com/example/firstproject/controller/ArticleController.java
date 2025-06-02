package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ArticleController {

  @Autowired  // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
  private ArticleRepository articleRepository;

  @GetMapping("/articles/new")
  public String newArticleForm(){
    return "articles/new";
  }

  @PostMapping("/articles/create")
  public String createArticle(ArticleForm form){
    log.info(form.toString());
    // System.out.println(form.toString()); // 디버깅용 코드 -> 로깅으로 대체

    // 1. dto 객체를 entity 타입으로 변환
    Article article = form.toEntity();
    log.info(article.toString());
    // System.out.println(article.toString());  // 디버깅용 코드 -> 로깅으로 대체

    // 2. repository에게 entity를 db안에 저장하게 함!
    Article saved = articleRepository.save(article);
    log.info(saved.toString());
    // System.out.println(saved.toString());  // 디버깅용 코드 -> 로깅으로 대체

    return "redirect:/articles/"+saved.getId();
  }

  @GetMapping("/articles/{id}")
  public String show(@PathVariable Long id, Model model){
    log.info("id = " + id);

    // 1. DB에서 id로 데이터를 가져옴 -> repository가 필요!
    Article articleEntity = articleRepository.findById(id).orElse(null);

    // 2. 가져온 데이터를 모델에 등록!
    model.addAttribute("article", articleEntity);

    // 3. 보여줄 페이지를 설정!
    return "articles/show";
  }

  @GetMapping("/articles")
  public String index(Model model){
    // 1. 전체 article을 가져온다 -> repository가 필요
    List<Article> articleEntityList = articleRepository.findAll();

    // 2. 가져온 article 묶음을 view로 전달한다 -> model이 필요
    model.addAttribute("articleList", articleEntityList);

    // 3. view 페이지를 설정
    return "articles/index";
  }

  @GetMapping("/articles/{id}/edit")
  public String edit(@PathVariable Long id, Model model){
    // 수정할 데이터를 가져오기!
    Article articleEntity = articleRepository.findById(id).orElse(null);

    // 모델에 데이터를 등록
    model.addAttribute("article", articleEntity);

    return "articles/edit"; // 뷰 페이지 설정
  }

  @PostMapping("/articles/update")
  public String update(ArticleForm form){
    log.info(form.toString());

    // 1. dto를 entity로 변환
    Article articleEntity=form.toEntity();
    log.info(articleEntity.toString());

    // 2. entity를 db로 저장
    // 2-1 : db에서 기존 데이터를 가져온다
    Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

    // 2-2 : 기존 데이터에 값이 있다면 갱신한다
    if (target != null) {
      articleRepository.save(articleEntity); // entity가 db로 갱신!
    }

    // 3. 수정 결과 페이지로 리다이렉트
    return "redirect:/articles/" + articleEntity.getId();
  }

  @GetMapping("articles/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes rttr){
    log.info("삭제요청이 들어왔습니다.");

    // 1. 삭제 대상을 가져온다
    Article target = articleRepository.findById(id).orElse(null);
    log.info(target.toString());

    // 2. 대상을 삭제한다
    if (target != null) {
      articleRepository.delete(target);
      rttr.addFlashAttribute("msg","delete completed");
    }

    // 3. 결과 페이지로 리다이렉트
    return "redirect:/articles";
  }


}
