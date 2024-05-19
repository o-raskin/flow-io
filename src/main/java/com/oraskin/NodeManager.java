package com.oraskin;

import java.util.Collection;
import java.util.Map;

public class NodeManager {

  private Node node;

  private Node createNode(Map<String, Object> requestBody) {
    if (this.node != null) {
      throw new RuntimeException("Node already exists");
    }
    String nodeId = (String) requestBody.get("node_id");
    Collection<String> nodeIds = (Collection<String>) requestBody.get("node_ids");
    this.node = new Node(nodeId, nodeIds);
    return this.node;
  }

  public Node getNode(Map<String, Object> requestBody) {
    if (this.node == null) {
      this.node = createNode(requestBody);
    }
    return this.node;
  }
}
