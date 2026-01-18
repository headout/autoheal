package com.autoheal.util;

import com.autoheal.model.LocatorType;

public final class CacheKeyGenerator {

    private static final String VERSION = "v1";

    private CacheKeyGenerator() {}

    public static String generate(
            LocatorType strategy,
            String rawLocator,
            String description,
            String context
    ) {
        String normalizedLocator = normalize(rawLocator);
        String descHash = hash(description);
        String safeContext = context == null ? "" : context;


        return String.join("|",
                VERSION,
                strategy.name().toLowerCase(),
                normalizedLocator,
                safeContext,
                descHash
        );
    }

    private static String normalize(String locator) {
        return locator
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static String hash(String input) {
        return Integer.toHexString(input.hashCode());
    }



}

