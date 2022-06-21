package redisson;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RListReactive;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

public class ReactiveMain {
  public static void main(String[] args){
    RedissonReactiveClient client = Redisson.create().reactive();

    RListReactive<String> list = client.getList("myList");
    list.add("test").then(
        list.get(0)
    ).subscribe(System.out::println);

    client.shutdown();
  }
}
