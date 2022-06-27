package com.example;

import org.json.JSONObject;

public class JsonMaker {
  public JSONObject make(String str){
    return new JSONObject().put("name", str);
  }
}
