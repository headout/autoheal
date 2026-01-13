package com.autoheal.util.dom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class HtmlOptimizer {

    private HtmlOptimizer() {
    }

    // ---------- CONFIG ----------

    private static final Set<String> REMOVE_TAGS = Set.of(
            "script", "style", "meta", "link", "svg", "canvas", "noscript"
    );

    private static final Set<String> ALWAYS_KEEP_ATTRS = Set.of(
            "id", "name", "value", "class",
            "data-qa-marker", "data-testid"
    );

    // Attribute must appear at least this many times to be retained
    private static final int ATTRIBUTE_FREQUENCY_THRESHOLD = 2;
    private static final int MAX_HTML_CHARS = 100_000;
// ~75k tokens (safe for 128k models, trivial for 1M)


    // ---------- CACHE ----------

    private static final Map<Integer, OptimizedHtmlResult> CACHE =
            new ConcurrentHashMap<>();

    // ---------- PUBLIC API ----------

    public static OptimizedHtmlResult optimizeWithMetrics(String html) {
        if (html == null || html.isEmpty()) {
            return new OptimizedHtmlResult("", null, null);
        }

        int cacheKey = html.hashCode();
        OptimizedHtmlResult cached = CACHE.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Document doc = Jsoup.parse(html);

        int originalNodeCount = doc.getAllElements().size();
        int originalSize = html.length();

        // 1️⃣ Remove unwanted tags
        for (String tag : REMOVE_TAGS) {
            doc.select(tag).remove();
        }

        // 2️⃣ Attribute frequency analysis
        Map<String, Integer> attrFrequency = new HashMap<>();

        for (Element el : doc.getAllElements()) {
            el.attributes().forEach(attr ->
                    attrFrequency.merge(attr.getKey(), 1, Integer::sum)
            );
        }

        Set<String> retainedAttrs = new HashSet<>(ALWAYS_KEEP_ATTRS);

        attrFrequency.forEach((attr, count) -> {
            if (count >= ATTRIBUTE_FREQUENCY_THRESHOLD) {
                retainedAttrs.add(attr);
            }
        });

        // 3️⃣ Prune attributes safely
        for (Element el : doc.getAllElements()) {
            Set<String> toRemove = new HashSet<>();

            el.attributes().forEach(attr -> {
                if (!retainedAttrs.contains(attr.getKey())) {
                    toRemove.add(attr.getKey());
                }
            });

            toRemove.forEach(el::removeAttr);
        }

        // After attribute pruning
        compactTextNodes(doc);

// Depth pruning
        pruneDeepDom(doc.body(), 0);

// Remove empty again (important after pruning)
        doc.select("*").removeIf(
                el -> el.children().isEmpty()
                        && el.text().trim().isEmpty()
                        && el.attributes().isEmpty()
        );

        String optimizedHtml = doc.body().html();

// Enforce final size cap
        optimizedHtml = enforceHtmlSizeLimit(optimizedHtml);

        DomMetrics metrics = new DomMetrics(
                originalNodeCount,
                doc.getAllElements().size(),
                originalSize,
                optimizedHtml.length()
        );

        OptimizedHtmlResult result =
                new OptimizedHtmlResult(optimizedHtml, metrics, retainedAttrs);

        CACHE.put(cacheKey, result);
        return result;
    }

    private static final int MAX_TEXT_LENGTH = 120;

    private static final int MAX_DOM_DEPTH = 14;

    private static void pruneDeepDom(Element el, int depth) {
        if (depth > MAX_DOM_DEPTH && el.attributes().isEmpty()) {
            el.remove();
            return;
        }
        for (Element child : new ArrayList<>(el.children())) {
            pruneDeepDom(child, depth + 1);
        }
    }

    private static String enforceHtmlSizeLimit(String html) {
        if (html.length() <= MAX_HTML_CHARS) {
            return html;
        }
        return html.substring(0, MAX_HTML_CHARS)
                + "\n<!-- HTML TRUNCATED DUE TO SIZE LIMIT -->";
    }



    private static void compactTextNodes(Document doc) {
        for (Element el : doc.getAllElements()) {
            if (!el.ownText().isEmpty()) {
                String text = el.ownText().trim();
                if (text.length() > MAX_TEXT_LENGTH) {
                    el.text(text.substring(0, MAX_TEXT_LENGTH) + "…");
                }
            }
        }
    }


    public void clearData() {
        CACHE.clear();
    }
}
