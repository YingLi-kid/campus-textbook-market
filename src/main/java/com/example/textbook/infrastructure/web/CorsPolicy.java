package com.example.textbook.infrastructure.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 跨域响应头策略。生产环境应把允许来源收窄为真实前端域名。
 */
public final class CorsPolicy {

    private final Set<String> allowedOrigins;

    public CorsPolicy(Set<String> allowedOrigins) {
        this.allowedOrigins = Set.copyOf(Objects.requireNonNull(allowedOrigins, "allowedOrigins must not be null"));
    }

    public static CorsPolicy development() {
        return new CorsPolicy(Set.of("http://localhost:5173"));
    }

    public boolean isAllowedOrigin(String origin) {
        return origin != null && allowedOrigins.contains(origin);
    }

    public boolean isPreflight(String method) {
        return "OPTIONS".equalsIgnoreCase(method);
    }

    public Map<String, String> responseHeaders(String origin) {
        if (!isAllowedOrigin(origin)) {
            return Map.of();
        }
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Access-Control-Allow-Origin", origin);
        headers.put("Vary", "Origin");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Access-Control-Max-Age", "3600");
        return Map.copyOf(headers);
    }
}
