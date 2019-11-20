package example;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    User save(User user) {
        return userRepository.save(user);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }
}
