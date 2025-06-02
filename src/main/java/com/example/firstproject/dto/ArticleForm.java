package com.example.firstproject.dto;


import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
  
  private Long id; // id 필드 추가! 처음 form 받을 때는 없어도 됨 (update를 위해 hidden으로 하며 추가)
 private String title;
 private String content;

  public Article toEntity() {
    return new Article(id, title, content); // 처음에는 id 자리에 null이었으나, id 필드 추가로 인한 코드 수정
  }
}
