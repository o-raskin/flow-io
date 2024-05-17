package com.oraskin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) throws JsonProcessingException {
    Scanner sc = new Scanner(System.in);
    ObjectMapper om = new ObjectMapper();
    while (true) {
      var requestJson = sc.nextLine();
      Map<String, Object> request = om.readValue(requestJson, HashMap.class);
      String src = (String) request.get("src");
      String dest = (String) request.get("dest");
      request.put("src", dest);
      request.put("dest", src);
      Map<String, Object> body = (Map<String, Object>) request.get("body");
      switch ((String)body.get("type")) {
        case "init" -> {
          body.put("type", "init_ok");
          body.put("msd_id", 123);
        }
        case "echo" -> {
          body.put("type", "echo_ok");
        }
      }
      body.put("in_reply_to", body.get("msg_id"));
      System.out.println(om.writeValueAsString(request));
    }
  }

}