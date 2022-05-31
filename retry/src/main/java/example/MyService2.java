package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyService2 {

  @Autowired
  private RestTemplate restTemplate;

  @Retryable( value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  String get(){
    System.out.println("try");
    String result = restTemplate.getForObject("http://notexist:8080", String.class);
    return result;
  }
}
