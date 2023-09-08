package org.example;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PredicateTest {

  public static void main(String[] args) {
    BiPredicate<Integer, Float> isLean = (age, weight)-> {
      if( age < 45)
        return weight < 60;
      else
        return weight < 70;
    };

    Predicate<Float> isLean2 = new CurriedPredicate<>(isLean, 40);
    System.out.println(isLean.test(40, 50f));
    System.out.println(isLean2.test(50f));
  }
}

class CurriedPredicate<A, B> implements Predicate<B>{

  BiPredicate<A, B> biPredicate;
  A age;

  public CurriedPredicate(BiPredicate<A, B> biPredicate, A age) {
    this.biPredicate = biPredicate;
    this.age = age;
  }

  @Override
  public boolean test(B aFloat) {
    return biPredicate.test(age, aFloat);
  }
}
