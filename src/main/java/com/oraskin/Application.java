package com.oraskin;

import com.oraskin.routing.RequestDispatcher;

public class Application {

  public static void main(String[] args) {
    RequestDispatcher requestProcessor = new RequestDispatcher();
    while (true) {
      requestProcessor.handleRequest();
    }
  }

}