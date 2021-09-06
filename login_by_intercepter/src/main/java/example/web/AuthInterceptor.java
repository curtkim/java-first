package example.web;

import example.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpSession session = request.getSession();
    if (session.getAttribute("login") == null) {
      System.out.println("current user is not logined");
      response.sendRedirect("/user/login");
      return false;
    }

    // 로그인한 사용자일 경우 Controller 호출
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) throws Exception {

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("login");
    modelAndView.addObject("user", user);

    System.out.println("post handle.......");
  }


}
