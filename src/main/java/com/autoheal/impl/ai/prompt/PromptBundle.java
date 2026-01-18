package com.autoheal.impl.ai.prompt;

import java.util.Map;

public class PromptBundle {

    private final Map<String, Object> data;

    public PromptBundle(Map<String, Object> data) {
        this.data = data;
    }

    public String system() {
        return get("system");
    }

    public String user(Object... args) {
        String template = get("user");
        return template != null ? String.format(template, args) : null;
    }

    public String assistant() {
        return get("assistant");
    }

    private String get(String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
}

