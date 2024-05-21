package com.oraskin.distsys;

import com.oraskin.distsys.routing.RequestDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {

  private final static Logger LOG = LogManager.getLogger(Application.class);

  public static void main(String[] args) {
    LOG.info("Application started");
    RequestDispatcher requestProcessor = new RequestDispatcher();
    while (true) {
      requestProcessor.handleRequest();
    }
  }

}