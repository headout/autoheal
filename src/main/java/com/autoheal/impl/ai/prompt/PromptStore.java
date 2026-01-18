package com.autoheal.impl.ai.prompt;

import java.util.Map;

public final class PromptStore {

    private final Map<String, Object> raw;

    public PromptStore(Map<String, Object> raw) {
        this.raw = raw;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSection(String... path) {
        Object current = raw;
        for (String key : path) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<String, Object>) current).get(key);
        }
        return (Map<String, Object>) current;
    }

    @SuppressWarnings("unchecked")
    public String getValue(String... path) {
        Object current = raw;
        for (String key : path) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<String, Object>) current).get(key);
        }
        return current != null ? current.toString() : null;
    }
}
