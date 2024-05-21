package com.oraskin.distsys.dto;

import java.util.Map;

public record Message(int id, String src, String dest, Map<String, Object> body) {

}
