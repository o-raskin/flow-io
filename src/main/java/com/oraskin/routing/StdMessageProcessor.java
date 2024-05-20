package com.oraskin.routing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraskin.dto.RequestDto;
import com.oraskin.dto.ResponseDto;
import java.util.Map;
import java.util.Scanner;

public class StdMessageProcessor {

  private final Scanner scanner;
  private final ObjectMapper objectMapper;

  public StdMessageProcessor() {
    this.objectMapper = new ObjectMapper();
    this.scanner = new Scanner(System.in);
  }

  public void respond(String src, String dest, Map<String, Object> responseBody) {
    try {
      ResponseDto responseDto = new ResponseDto(src, dest, responseBody);
      System.out.println(objectMapper.writeValueAsString(responseDto));
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe.getMessage());
    }
  }

  public RequestDto receive() {
    try {
      var json = scanner.nextLine();
      return objectMapper.readValue(json, RequestDto.class);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe.getMessage());
    }
  }

}
