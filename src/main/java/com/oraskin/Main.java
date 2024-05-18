package com.oraskin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {

  public static void main(String[] args) throws JsonProcessingException {
    Scanner sc = new Scanner(System.in);
    ObjectMapper om = new ObjectMapper();
    int counter = 0;
    String nodeId = null;
    List<String> nodeIds = new ArrayList<>();
    while (true) {
      try {
        var requestJson = sc.nextLine();
        Map<String, Object> request = om.readValue(requestJson, HashMap.class);
        String src = (String) request.get("src");
        String dest = (String) request.get("dest");
        request.put("src", dest);
        request.put("dest", src);
        Map<String, Object> body = (Map<String, Object>) request.get("body");
        String bodyType = (String) body.get("type");
        switch (bodyType) {
          case "init" -> {
            body.put("type", "init_ok");
            body.put("msd_id", 123);
            nodeId = (String) body.get("node_id");
            nodeIds.addAll((Collection<String>)body.get("node_id"));
          }
          case "echo" -> {
            body.put("type", "echo_ok");
          }
          case "generate" -> {
            counter++;
            // 1. отправить запросы на все ноды и ДОЖДАТЬСЯ ответов
            // int id =... ;
            for (String nId : nodeIds) {
              request.put("dest", nId);
              body.put("proposal", counter);
              body.put("type", "next");
              System.out.println(om.writeValueAsString(request));
            }
            body.put("type", "generate_ok");
            body.put("id", id);

          }
          case "next" -> {
            body.put("type", "next");
            int proposal = (int) body.get("proposal");
            int response = Math.max(counter, proposal);
            counter = response;
            body.put("type", "next_ok");
            body.put("response", response);
            System.out.println(requestJson);
          }
          case "next_ok" -> {
            // ждем ответы и выбираем максимальный proposal
            // если максимальный == proposal -> reuse 'NEXT'
            // если максимальный != proposal -> do response
            counter = body.get()
            System.out.println(om.writeValueAsString(request));
            //
          }
        }
        body.put("in_reply_to", body.get("msg_id"));

        System.out.println(om.writeValueAsString(request));


        request.put("dest", "n2");
        body.put("type", "next");
        body.put("msg_id", 5051);
        System.out.println(om.writeValueAsString(request));

      } catch (Exception e) {
        System.out.println("Java Error: " + e.getMessage());
      }
    }
  }

}