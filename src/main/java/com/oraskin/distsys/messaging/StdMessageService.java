package com.oraskin.distsys.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraskin.distsys.dto.Message;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StdMessageService {

  private final static Logger LOG = LogManager.getLogger(StdMessageService.class);

  private final Scanner scanner;
  private final ObjectMapper objectMapper;

  public StdMessageService() {
    this.objectMapper = new ObjectMapper();
    this.scanner = new Scanner(System.in);
  }

  public void send(String src, String dest, Map<String, Object> responseBody) {
    try {
      Message message = new Message(0, src, dest, responseBody);
      String json = objectMapper.writeValueAsString(message);
      LOG.info(json);
      System.out.println(json);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe.getMessage());
    }
  }

  public Message receive() {
    try {
      var json = scanner.nextLine();
      if (json == null || json.isEmpty()) {
        return null;
      }
      LOG.info(json);
      return objectMapper.readValue(json, Message.class);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe.getMessage());
    }
  }

}
