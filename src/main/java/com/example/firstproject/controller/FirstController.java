package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

  @GetMapping("/hi")
  public String niceToMeetYou(Model model){
    model.addAttribute("username", "민상");
    return "greetings";  //   templates/greetings.mustache -> 템플릿 안에 해당 파일을 찾아 브라우저로 전송!
  }

  @GetMapping("/bye")
  public String seeYouNext(Model model) {
    model.addAttribute("nickname","홍길동");
    return "goodbye";
  }
}
