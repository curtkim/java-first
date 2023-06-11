package com.example;

import it.unimi.dsi.fastutil.longs.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@State(Scope.Benchmark)
public class LongIntMapBenchmark {

  final int SIZE = 3_000_000;

  Long2IntMap fastMap;
  Long2IntMap fastAVLMap;

  Map<Long, Integer> normalMap;


  @Setup(Level.Trial)
  public void setUp(){
    fastMap = new Long2IntOpenHashMap(SIZE);
    fastAVLMap = new Long2IntAVLTreeMap();
    normalMap = new HashMap<>(SIZE);

    fillData(fastMap, "fast");
    fillData(fastAVLMap, "fastAVL");
    fillData(normalMap, "normal");
  }

  void fillData(Map<Long, Integer> map, String name){
    long start = System.currentTimeMillis();
    for(int i = 0; i < SIZE; i++)
      map.put((long)i, i);
    System.out.println(String.format("%s done %d ms", name, System.currentTimeMillis()-start));

    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();;
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(map);
      System.out.println(String.format("%s byte size=%d", name, bos.toByteArray().length));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Benchmark
  public void fast(Blackhole bh) {
    int key = (int)(Math.random()*SIZE);
    bh.consume(fastMap.get(key));
  }

  @Benchmark
  public void fastavl(Blackhole bh) {
    int key = (int)(Math.random()*SIZE);
    bh.consume(fastAVLMap.get(key));
  }

  @Benchmark
  public void normal(Blackhole bh) {
    int key = (int)(Math.random()*SIZE);
    bh.consume(normalMap.get(key));
  }
}
