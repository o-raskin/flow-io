package com.oraskin.dto;

import java.util.Map;

public record RequestDto(int id, String src, String dest, Map<String, Object> body) {

}
