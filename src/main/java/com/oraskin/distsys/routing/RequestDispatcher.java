package com.oraskin.distsys.routing;

import static com.oraskin.distsys.messaging.Constants.TYPE_PARAM;

import com.oraskin.distsys.dto.Message;
import com.oraskin.distsys.messaging.StdMessageService;
import com.oraskin.distsys.node.Node;
import com.oraskin.distsys.node.NodeManager;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestDispatcher {

  private final static Logger LOG = LogManager.getLogger(RequestDispatcher.class);

  private final NodeManager nodeManager;
  private final StdMessageService stdMessageProcessor;
  private final Executor executor;

  public RequestDispatcher() {
    this.nodeManager = new NodeManager();
    this.stdMessageProcessor = new StdMessageService();
    this.executor = Executors.newVirtualThreadPerTaskExecutor();
  }

  public void handleRequest() {
    Message request = stdMessageProcessor.receive();
    if (request == null) {
      return;
    }
    CompletableFuture.runAsync(() -> {
          Map<String, Object> requestBody = request.body();
          Node node = nodeManager.getNode(requestBody);
          // todo: redo with reflection (resolving by method names)
          String bodyType = (String) requestBody.get(TYPE_PARAM);
          Map<String, Object> responseBody = switch (bodyType) {
            case "init" -> node.init(requestBody);
            case "echo" -> node.echo(requestBody);
            case "generate" -> node.generate(requestBody);
            case "next" -> node.next(requestBody);
            case "next_ok" -> node.nextOk();
            default -> throw new IllegalStateException("Unexpected request type: " + bodyType);
          };
          stdMessageProcessor.send(node.getId(), request.src(), responseBody);
        }, executor)
        .exceptionally(throwable -> {
          LOG.error(throwable.getMessage());
          return null;
        });
  }

}
