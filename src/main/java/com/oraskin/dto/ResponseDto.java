package com.oraskin.dto;

import java.util.Map;

public record ResponseDto(String src, String dest, Map<String, Object> body) {

}
