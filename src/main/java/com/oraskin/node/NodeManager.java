package com.oraskin.node;

import java.util.Collection;
import java.util.Map;

public class NodeManager {

  private static final String INIT_REQUEST_TYPE_VALUE = "init";
  private static final String TYPE_REQUEST_PARAM = "type";
  private static final String NODE_ID_REQUEST_PARAM = "node_id";
  private static final String NODE_IDS_REQUEST_PARAM = "node_ids";

  private Node node;

  public Node getNode(Map<String, Object> requestBody) {
    if (this.node == null) {
      if (!INIT_REQUEST_TYPE_VALUE.equals(requestBody.get(TYPE_REQUEST_PARAM))) {
        throw new RuntimeException(String.format("Wrong type of request, expected '%s'", INIT_REQUEST_TYPE_VALUE));
      }
      this.node = createNode(requestBody);
    }
    return this.node;
  }

  private Node createNode(Map<String, Object> requestBody) {
    if (this.node != null) {
      throw new RuntimeException("Node already exists");
    }
    String nodeId = (String) requestBody.get(NODE_ID_REQUEST_PARAM);
    if (nodeId == null) {
      throw new RuntimeException(String.format("'%s' is required", NODE_ID_REQUEST_PARAM));
    }
    Collection<String> nodeIds = (Collection<String>) requestBody.get(NODE_IDS_REQUEST_PARAM);
    if (nodeIds == null) {
      throw new RuntimeException(String.format("'%s' is required", NODE_IDS_REQUEST_PARAM));
    }
    this.node = new Node(nodeId, nodeIds);
    return this.node;
  }
}
