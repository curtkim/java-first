package org.example;

import org.eclipse.collections.api.factory.map.primitive.ImmutableLongIntMapFactory;
import org.eclipse.collections.api.map.primitive.ImmutableLongIntMap;
import org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongIntMapFactoryImpl;
import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

public class Main {
  public static void main(String[] args) {
    LongIntHashMap map = LongIntHashMap.newWithKeysValues(1, 1, 2, 2 );
    System.out.println(map);
    System.out.println(map.get(2));
    map.compact();
    System.out.println(map.get(2));
    System.out.println(map.containsKey(3));

    ImmutableLongIntMap map3 = map.toImmutable();
    System.out.println(map3.get(2));

    LongObjectHashMap<LongIntHashMap> g = LongObjectHashMap.newWithKeysValues(1, map);
    System.out.println(g.containsKey(0));

    //map.toImmutable();
    //ImmutableLongIntMap map2 = ImmutableLongIntMapFactoryImpl.INSTANCE.with();
  }
}