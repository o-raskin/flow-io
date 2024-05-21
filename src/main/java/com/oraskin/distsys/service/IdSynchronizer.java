package com.oraskin.distsys.service;

import static com.oraskin.distsys.messaging.Constants.NEXT_TYPE_VALUE;
import static com.oraskin.distsys.messaging.Constants.PROPOSAL_PARAM;
import static com.oraskin.distsys.messaging.Constants.TYPE_PARAM;

import com.oraskin.distsys.messaging.StdMessageService;
import com.oraskin.distsys.messaging.StdMessageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class IdSynchronizer {

  private final StdMessageService stdMessageService;
  private final String nodeId;
  private final List<String> nodeIds;
  private final AtomicInteger counter = new AtomicInteger(0);

  public IdSynchronizer(String nodeId, List<String> nodeIds) {
    this.nodeId = nodeId;
    this.nodeIds = nodeIds;
    this.stdMessageService = new StdMessageService();
  }

  public int getNewId() {
    int clusterMaxId;
    do {
      CompletableFuture<Void>[] futures = new CompletableFuture[nodeIds.size()];
      for (int i = 0; i < nodeIds.size(); i++) {
        String dest = nodeIds.get(i);
        futures[i] = CompletableFuture.runAsync(() -> {
          Map<String, Object> requestBody = new HashMap<>();
          requestBody.put(PROPOSAL_PARAM, counter.incrementAndGet());
          requestBody.put(TYPE_PARAM, NEXT_TYPE_VALUE);
          stdMessageService.send(nodeId, dest, requestBody);
        });
      }
      CompletableFuture.allOf(futures)
          .thenRun(() -> {
            // wait response in PARALLEL THREAD (do not block main from receiving extra requests)
            // how we can 'understand' when we should READ
          })
          .join();
      clusterMaxId = counter.get();
//      try {
//        for (Future<Void> future : futures) {
//          max = Math.max(max, future.get());
//        }
//      } catch (ExecutionException | InterruptedException e) {
//        throw new RuntimeException(e);
//      }
    } while (counter.get() != clusterMaxId);
    return counter.get();
  }
}
