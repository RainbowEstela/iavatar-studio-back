package com.estelair.iavatarstudioback.dto;

import java.util.List;

public record LoginResponse(String username, List<String> authorities, String token) {
}
