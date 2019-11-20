package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public User add(@RequestParam final String name) {
        User user = new User();
        user.setName(name);
        return userService.save(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

}
