package example;

public class UserService {
  public User getUser(User requested) {
    if( requested.password == null)
      return null;

    User user = new User();
    user.username = "curt";
    user.password = "curt";
    return user;
  }
}
