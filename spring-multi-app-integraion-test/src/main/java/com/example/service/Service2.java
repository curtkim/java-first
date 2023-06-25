package com.example.service;

import com.example.dao.ADao;
import com.example.dao.BDao;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Service2 {

  private ADao aDao;
  private BDao bDao;

  public String get() {
    return aDao.getA() + bDao.getB();
  }
}
