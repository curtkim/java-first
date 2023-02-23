package redisson;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

public class Main1 {
  public static void main(String[] args){
    RedissonClient client = Redisson.create();

    RList<String> myList = client.getList("myList");
    myList.add("test");

    System.out.println(myList.get(0));
    System.out.println(myList.subList(0,2));
    System.out.println(myList.size());

    client.shutdown();
  }
}
