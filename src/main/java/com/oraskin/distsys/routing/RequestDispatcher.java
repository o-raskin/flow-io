package com.oraskin.distsys.routing;

import static com.oraskin.distsys.messaging.Constants.TYPE_PARAM;

import com.oraskin.distsys.dto.Message;
import com.oraskin.distsys.messaging.StdMessageService;
import com.oraskin.distsys.node.Node;
import com.oraskin.distsys.node.NodeManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
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
      if (!(request.body() instanceof Map requestBody)) {
        throw new RuntimeException("Invalid request body");
      }
      Node node = nodeManager.getNode(requestBody);
      Map<String, Method> methodNameToInstance = Arrays.stream(node.getClass().getDeclaredMethods())
          .peek(m -> m.setAccessible(true))
          .collect(Collectors.toMap(Method::getName, m -> m));
      try {
        String bodyType = (String) requestBody.get(TYPE_PARAM);
        Method targetMethod = methodNameToInstance.get(bodyType);
        Object responseBody = targetMethod.invoke(node, requestBody);
        stdMessageProcessor.send(node.getId(), request.src(), responseBody);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Node invocation failed", e);
      }
    }, executor).exceptionally(throwable -> {
      LOG.error(throwable.getMessage());
      return null;
    });
  }

}
