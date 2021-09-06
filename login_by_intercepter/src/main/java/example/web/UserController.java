package example.web;

import example.User;
import example.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

  @Autowired
  UserService service;

  // 로그인 폼을 띄우는 부분
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String loginForm() {
    return "loginForm";
  }

  // 로그인 처리하는 부분
  @RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
  public String loginCheck(HttpSession session, User form) {
    if (session.getAttribute("login") != null)
      session.removeAttribute("login"); // 기존값을 제거해 준다.

    User user = service.getUser(form);

    if (user != null) { // 로그인 성공
      session.setAttribute("login", user);
      return "redirect:/domain/";
    } else { // 로그인에 실패한 경우
      return "redirect:/user/login";
    }
  }

  // 로그아웃 하는 부분
  @RequestMapping(value = "/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/user/login";
  }
}