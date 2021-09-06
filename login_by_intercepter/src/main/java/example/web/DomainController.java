package example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("domain")
public class DomainController {
  @RequestMapping("/")
  public String index() {
    return "domain";
  }

}
