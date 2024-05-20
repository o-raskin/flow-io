package com.oraskin.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraskin.dto.RequestDto;
import java.util.Map;

public class StdClient {

  private final String nodeId;
  private final ObjectMapper objectMapper;

  public StdClient(String nodeId) {
    this.nodeId = nodeId;
    this.objectMapper = new ObjectMapper();
  }

  public void sendRequest(String dest, Map<String, Object> requestBody) {
    try {
      RequestDto request = new RequestDto(0, nodeId, dest, requestBody);
      System.out.println(objectMapper.writeValueAsString(request));
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe.getMessage());
    }
  }
}
