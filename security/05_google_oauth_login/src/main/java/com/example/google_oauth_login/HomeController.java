package com.example.google_oauth_login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
public class HomeController {

  @GetMapping("/hello")
  public String hello(HttpServletRequest request, HttpServletResponse response) {
    return "hello";
  }

  @GetMapping("/auth/google")
  public void startGoogleAuth(HttpServletRequest request, HttpServletResponse response) {
    System.out.println("Google Oauth Login Initiated");
    String uri = request.getRequestURI();
    System.out.println(uri);
  }

  // On Successfull OAuth Google will return principal object
  // Principal Object Consist of username ,name ,email depending on scope mention in yml.
  @GetMapping("/oauth2/callback/google")
  public Principal callbackGoogle(Principal principal) {
    //System.out.println(principal.toString());
    return principal;
  }
}
