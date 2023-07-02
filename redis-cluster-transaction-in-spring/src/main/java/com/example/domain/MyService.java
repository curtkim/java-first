package com.example.domain;

import com.example.lettuce.data.LettuceTemplate;
import io.lettuce.core.TransactionResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class MyService {

  private LettuceTemplate lettuceTemplate;
  private SummaryDao summaryDao;
  private SliceDao sliceDao;

  public void update(String id, String summary, List<String> slices) {
    String summaryKey = String.format("summary:{%s}", id);
    String slicesKey = String.format("slices:{%s}", id);
    lettuceTemplate.execute(summaryKey, commands -> {
      summaryDao.watch(summaryKey);
      String summaryValue = summaryDao.get(summaryKey);

      commands.multi();
      summaryDao.set(summaryKey, summaryValue+summary);
      sliceDao.add(slicesKey, slices);
      TransactionResult txResult = commands.exec();

      log.info("wasDiscarded = {}", txResult.wasDiscarded());
      if( txResult.wasDiscarded())
        throw new RuntimeException("optimistic lock fail(watched key changed)");
      return null;
    });
  }

  public MyRecord get(String id){
    String summaryKey = String.format("summary:{%s}", id);
    String slicesKey = String.format("slices:{%s}", id);

    return (MyRecord) lettuceTemplate.execute(summaryKey, conn ->{
      return new MyRecord(summaryDao.get(summaryKey), sliceDao.get(slicesKey));
    });
  }

}
