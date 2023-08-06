import agili.pocs.JwtTokenUtil;
import org.junit.Test;

public class JwtTokenUtilTest {

  @Test
  public void test() {
    /*
    https://www.javainuse.com/jwtgenerator
    {
    "Issuer": "Issuer",
    "Issued At": "2023-08-06T13:00:11.221Z",
    "Expiration": "2023-08-06T20:00:11.221Z",
    "Username": "JavaInUse",
    "Role": "Admin"
    }
    */
    //String token = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY5MTMyNjgxMSwiaWF0IjoxNjkxMzI2ODExfQ.jdTcw9Y0UihXaM3aMgLcDA31DYHQ3pEuvXJCUOSYW9b2MbiYU5cRAKXS8sbMd1wVZLdUdw_hPrP45MEJVxvh9w";
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY5MTM1MjAxMSwiaWF0IjoxNjkxMzI2ODExfQ.A9UqJ-VZ5GFtyAG4lXRSHF0BpFFNCMLpqpZ8Ag3Wz-RIfN3kkqMGhJ1oGjRJ5Fp-dKMzdaDYFgfAMszf6BLaWQ";

    JwtTokenUtil util = new JwtTokenUtil();
    util.setSecret("javainuse-secret-key");
    System.out.println(util.getAllClaimsFromToken(token));
    // {Role=Admin, Issuer=Issuer, Username=JavaInUse, exp=1691352011, iat=1691326811}
  }
}
