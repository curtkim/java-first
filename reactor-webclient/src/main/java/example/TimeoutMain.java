package example;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class TimeoutMain {

  public static void main(String[] args){
    /*
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(5000);
    requestFactory.setReadTimeout(5000);
    */
    RestTemplate restTemplate = new RestTemplate(create());

    ResponseEntity<String> msg = restTemplate.getForEntity("http://localhost:8080/?msg=a&delay=4000", String.class);
    System.out.println(msg.getBody());
  }

  static int POOL_MAX_TOTAL = 10000;
  static int MAX_PER_ROUTE = 1000;

  public static ClientHttpRequestFactory create() {
    HttpClientBuilder builder = HttpClientBuilder.create();

    // 1. connection manager
    PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
    manager.setValidateAfterInactivity(1000);
    manager.setDefaultConnectionConfig(ConnectionConfig.custom().setBufferSize(8 * 1024).build());
    manager.setMaxTotal(POOL_MAX_TOTAL);
    manager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
    builder.setConnectionManager(manager);

    // 2. retryHandler
    builder.setRetryHandler(
        new HttpRequestRetryHandler() {
          @Override
          public boolean retryRequest(
              IOException exception, int executionCount, HttpContext context) {
            // TODO 어떤 url을 호출하다 실패한 것인지 알고 싶다.
            System.err.println(exception + " " + executionCount);
            if (executionCount > 1) {
              return false;
            }
            return true;
          }
        });

    // 3. default request config
    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
    requestConfigBuilder.setConnectTimeout(3000);
    requestConfigBuilder.setSocketTimeout(3000);
    builder.setDefaultRequestConfig(requestConfigBuilder.build());

    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(builder.build());
    factory.setReadTimeout(5000);
    return factory;
  }
}
