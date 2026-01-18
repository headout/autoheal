package com.autoheal.model;

/**
 * Enum representing all locator strategies supported by AutoHeal
 * (Selenium + Playwright)
 */
public enum LocatorType {

    /* =======================
     * Selenium-style locators
     * ======================= */

    CSS_SELECTOR("CSS Selector"),
    XPATH("XPath"),
    ID("ID"),
    NAME("Name"),
    CLASS_NAME("Class Name"),
    TAG_NAME("Tag Name"),
    LINK_TEXT("Link Text"),
    PARTIAL_LINK_TEXT("Partial Link Text"),

    /* =======================
     * Playwright-style locators
     * ======================= */

    GET_BY_ROLE("Get By Role"),
    GET_BY_LABEL("Get By Label"),
    GET_BY_PLACEHOLDER("Get By Placeholder"),
    GET_BY_TEXT("Get By Text"),
    GET_BY_ALT_TEXT("Get By Alt Text"),
    GET_BY_TITLE("Get By Title"),
    GET_BY_TEST_ID("Get By Test ID");

    private final String displayName;

    LocatorType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Human-readable name (for logs, UI, reports)
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Reporting-friendly name (snake_case)
     */
    public String getReportName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
