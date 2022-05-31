package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RetryTemplate retryTemplate;

  public String get(){
    return retryTemplate.execute(arg->{
      System.out.println(String.format("retrycount = %d", arg.getRetryCount()));
      return getInner();
    });
  }

  String getInner(){
    String result = restTemplate.getForObject("http://notexist:8080", String.class);
    return result;
  }
}
