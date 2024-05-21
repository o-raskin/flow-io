package com.oraskin.distsys.node;

import static com.oraskin.distsys.messaging.Constants.INIT_TYPE_VALUE;
import static com.oraskin.distsys.messaging.Constants.TYPE_PARAM;

import java.util.Collection;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NodeManager {

  private final static Logger LOG = LogManager.getLogger(NodeManager.class);

  private static final String NODE_ID_REQUEST_PARAM = "node_id";
  private static final String NODE_IDS_REQUEST_PARAM = "node_ids";

  private Node node;

  public Node getNode(Map<String, Object> requestBody) {
    if (this.node == null) {
      if (!INIT_TYPE_VALUE.equals(requestBody.get(TYPE_PARAM))) {
        throw new RuntimeException(String.format("Wrong type of request, expected '%s'", INIT_TYPE_VALUE));
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
    LOG.info("Node initialized");
    return this.node;
  }
}
