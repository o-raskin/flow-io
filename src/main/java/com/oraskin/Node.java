package com.oraskin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oraskin.dto.RequestDto;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Node {

  private final String id;

  // includes current node id
  private final Set<String> nodeIds;
  private final IdGenerator idGenerator;

  public Node(String nodeId, Collection<String> nodeIds) {
    this.id = nodeId;
    this.nodeIds = new HashSet<>(nodeIds);
    this.idGenerator = new IdGenerator();
  }

  public Map<String, Object> init(Map<String, Object> requestBody) {
    return Map.of(
        "type", "init_ok",
        "in_reply_to", requestBody.get("msg_id"),
        "msd_id", 123);
  }

  public Map<String, Object> echo(Map<String, Object> requestBody) {
    return Map.of(
        "type", "echo_ok",
        "in_reply_to", requestBody.get("msg_id"),
        "echo", requestBody.get("echo"));
  }

  // todo: send requests to node cluster and wait info about ids
  public Map<String, Object> generate(Map<String, Object> requestBody) throws JsonProcessingException {
//    counter++;
//    for (String nodeId : nodeIds) {
//      Map<String, Object> idRequestBody = new HashMap<>();
//      idRequestBody.put("proposal", counter);
//      idRequestBody.put("type", "next");
//      RequestDto idRequest = new RequestDto(0, id, nodeId, idRequestBody);
//      System.out.println(objectMapper.writeValueAsString(idRequest));
//    }

    return Map.of(
        "in_reply_to", requestBody.get("msg_id"),
        "type", "generate_ok",
        "id", UUID.randomUUID());
  }

  public Map<String, Object> next(RequestDto request) throws JsonProcessingException {
//    Map<String, Object> requestBody = request.body();
//    requestBody.put("type", "next");
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
  public Map<String, Object> nextOk() {
    return Map.of();
  }

  public String getId() {
    return id;
  }
}
