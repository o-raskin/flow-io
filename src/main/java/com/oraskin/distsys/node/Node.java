package com.oraskin.distsys.node;

import static com.oraskin.distsys.messaging.Constants.ECHO_PARAM;
import static com.oraskin.distsys.messaging.Constants.ID_PARAM;
import static com.oraskin.distsys.messaging.Constants.IN_REPLY_TO_PARAM;
import static com.oraskin.distsys.messaging.Constants.MSG_ID_INIT_RESPONSE_VALUE;
import static com.oraskin.distsys.messaging.Constants.MSG_ID_PARAM;
import static com.oraskin.distsys.messaging.Constants.OK_TYPE_SUFFIX;
import static com.oraskin.distsys.messaging.Constants.TYPE_PARAM;

import com.oraskin.distsys.service.IdSynchronizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Node {

  private final String id;
  private final List<String> nodeIds; // includes current node id
  private final IdSynchronizer idSynchronizer;

  Node(String nodeId, Collection<String> nodeIds) {
    this.id = nodeId;
    this.nodeIds = new ArrayList<>(nodeIds);
    this.idSynchronizer = new IdSynchronizer(this.id, this.nodeIds);
  }

  public Map<String, Object> init(Map<String, Object> requestBody) {
    if (!requestBody.containsKey(MSG_ID_PARAM)) {
      throw new RuntimeException(String.format("Request body must contain '%s'", MSG_ID_PARAM));
    }
    return Map.of(
        TYPE_PARAM, requestBody.get(TYPE_PARAM) + OK_TYPE_SUFFIX,
        IN_REPLY_TO_PARAM, requestBody.get(MSG_ID_PARAM),
        MSG_ID_PARAM, MSG_ID_INIT_RESPONSE_VALUE);
  }

  public Map<String, Object> echo(Map<String, Object> requestBody) {
    if (!requestBody.containsKey(MSG_ID_PARAM)) {
      throw new RuntimeException(String.format("Request body must contain '%s'", MSG_ID_PARAM));
    }
    if (!requestBody.containsKey(ECHO_PARAM)) {
      throw new RuntimeException(String.format("Request body must contain '%s'", ECHO_PARAM));
    }
    return Map.of(
        TYPE_PARAM, requestBody.get(TYPE_PARAM) + OK_TYPE_SUFFIX,
        IN_REPLY_TO_PARAM, requestBody.get(MSG_ID_PARAM),
        ECHO_PARAM, requestBody.get(ECHO_PARAM));
  }

  // todo: send requests to node cluster and wait info about ids
  public Map<String, Object> generate(Map<String, Object> requestBody) {
    if (!requestBody.containsKey(MSG_ID_PARAM)) {
      throw new RuntimeException(String.format("Request body must contain '%s'", MSG_ID_PARAM));
    }

    int id = idSynchronizer.getNewId();

//    counter++;
//    for (String nodeId : nodeIds) {
//      Map<String, Object> idRequestBody = new HashMap<>();
//      idRequestBody.put("proposal", counter);
//      idRequestBody.put("type", "next");
//      RequestDto idRequest = new RequestDto(0, id, nodeId, idRequestBody);
//      System.out.println(objectMapper.writeValueAsString(idRequest));
//    }

    return Map.of(
        IN_REPLY_TO_PARAM, requestBody.get(MSG_ID_PARAM),
        TYPE_PARAM, requestBody.get(TYPE_PARAM) + OK_TYPE_SUFFIX,
        ID_PARAM, UUID.randomUUID());
  }

  public Map<String, Object> next(Map<String, Object> requestBody) {
//    Map<String, Object> requestBody = request.body();
//    int proposal = (int) requestBody.get("proposal");
//    int response = Math.max(counter, proposal);
//    counter = response;
//    requestBody.put("type", "next_ok");
//    requestBody.put("response", response);
    return Map.of();
  }

  // todo: wait responses and choose max proposal
  //    -> if (our proposal is not max) -> try again 'next' (increment and re-request)
  //    -> else -> respond
  public Map<String, Object> next_ok() {
    return Map.of();
  }

  public String getId() {
    return id;
  }
}
