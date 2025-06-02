package com.example.firstproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity  // db가 해당 객체를 인식 가능 (해당 클래스로 테이블을 만든다)
@AllArgsConstructor
@NoArgsConstructor //디폴트 생성자를 추가해줌
@ToString
@Getter
public class Article {

  @Id // 대표값을 지정! LIKE 주민등록번호
  // @GeneratedValue   // 1,2,3, ..... 자동 생성 어노테이션!
  @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id를 자동 생성 어노테이션
  private Long id;

  @Column
  private String title;

  @Column
  private String content;

  public void patch(Article article) {
    if (article.title != null) {
      this.title = article.title;
    }

    if (article.content != null) {
      this.content = article.content;
    }
  }
}
