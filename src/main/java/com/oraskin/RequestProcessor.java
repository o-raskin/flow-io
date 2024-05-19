package com.oraskin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraskin.dto.RequestDto;
import com.oraskin.dto.ResponseDto;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class RequestProcessor {

  private final Scanner scanner;
  private final ObjectMapper objectMapper;
  private final NodeManager nodeManager;

  public RequestProcessor() {
    this.scanner = new Scanner(System.in);
    this.objectMapper = new ObjectMapper();
    this.nodeManager = new NodeManager();
  }

  public void start() {
    try {
      while (true) {
        var json = scanner.nextLine();
        RequestDto request = objectMapper.readValue(json, RequestDto.class);
        route(request);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void route(RequestDto request) throws JsonProcessingException {
    Map<String, Object> requestBody = request.body();
    String bodyType = (String) requestBody.get("type");
    Node node = nodeManager.getNode(requestBody);
    Map<String, Object> reponseBody = switch (bodyType) {
      case "init" -> node.init(requestBody);
      case "echo" -> node.echo(requestBody);
      case "generate" -> node.generate(requestBody);
      case "next" -> node.next(request);
      case "next_ok" -> node.nextOk();
      default -> throw new IllegalStateException("Unexpected 'type': " + bodyType);
    };
    respond(node.getId(), request.src(), reponseBody);
  }

  private void respond(String src, String dest, Map<String, Object> responseBody) throws JsonProcessingException {
    ResponseDto response = new ResponseDto(src, dest, responseBody);
    System.out.println(objectMapper.writeValueAsString(response));
  }

}
