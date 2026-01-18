package com.autoheal.model;

public enum PromptType {
    DOM,
    VISUAL;

    /**
     * Get user-friendly display name
     *
     * @return Display name of the framework
     */
    public String getDisplayName() {
        return switch (this) {
            case DOM -> "DOM";
            case VISUAL -> "VISUAL DIFF";
        };
    }
}
