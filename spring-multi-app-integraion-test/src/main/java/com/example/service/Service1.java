package com.example.service;

import com.example.big.BigObject;
import com.example.dao.ADao;
import com.example.dao.BDao;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Service1 {

  private ADao aDao;
  private BDao bDao;
  private BigObject bigObject;

  public String get(){
    return aDao.getA()+ bDao.getB()+ bigObject.getBig();
  }
}
