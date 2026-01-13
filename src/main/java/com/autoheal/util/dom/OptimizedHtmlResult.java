package com.autoheal.util.dom;


import java.util.Set;

public final class OptimizedHtmlResult {

    private final String optimizedHtml;
    private final DomMetrics metrics;
    private final Set<String> retainedAttributes;

    public OptimizedHtmlResult(String optimizedHtml,
                               DomMetrics metrics,
                               Set<String> retainedAttributes) {
        this.optimizedHtml = optimizedHtml;
        this.metrics = metrics;
        this.retainedAttributes = retainedAttributes;
    }

    public String getOptimizedHtml() {
        return optimizedHtml;
    }

    public DomMetrics getMetrics() {
        return metrics;
    }

    public Set<String> getRetainedAttributes() {
        return retainedAttributes;
    }
}

