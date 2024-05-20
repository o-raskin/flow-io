package com.oraskin.routing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oraskin.dto.RequestDto;
import com.oraskin.node.Node;
import com.oraskin.node.NodeManager;
import java.util.Map;

public class RequestDispatcher {

  private static final String TYPE_REQUEST_PARAM = "type";
  private final NodeManager nodeManager;
  private final StdMessageProcessor stdMessageProcessor;

  public RequestDispatcher() {
    this.nodeManager = new NodeManager();
    this.stdMessageProcessor = new StdMessageProcessor();
  }

  public void handleRequest() {
    try {
      RequestDto request = stdMessageProcessor.receive();
      Map<String, Object> requestBody = request.body();
      String bodyType = (String) requestBody.get(TYPE_REQUEST_PARAM);
      Node node = nodeManager.getNode(requestBody);
      // todo: try to redo with reflection (resolving by method names)
      Map<String, Object> responseBody = switch (bodyType) {
        case "init" -> node.init(requestBody);
        case "echo" -> node.echo(requestBody);
        case "generate" -> node.generate(requestBody);
        case "next" -> node.next(request);
        case "next_ok" -> node.nextOk();
        default -> throw new IllegalStateException("Unexpected request type: " + bodyType);
      };
      stdMessageProcessor.respond(node.getId(), request.src(), responseBody);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
