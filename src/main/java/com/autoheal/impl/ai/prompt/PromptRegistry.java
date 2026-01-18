package com.autoheal.impl.ai.prompt;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class PromptRegistry {
    private static volatile PromptRegistry INSTANCE;
    private final PromptStore store;

    private PromptRegistry() {
        this.store = load();
    }

    public static PromptRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (PromptRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PromptRegistry();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private PromptStore load() {
        InputStream is = PromptRegistry.class
                .getClassLoader()
                .getResourceAsStream("prompt/prompt.yaml");

        if (is == null) {
            System.err.println("⚠️ prompt/prompt.yaml not found in classpath");
            return new PromptStore(new HashMap<>());
        }

        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);

        Yaml yaml = new Yaml(options);
        Map<String, Object> data = yaml.load(is);

        return new PromptStore(data);
    }

    // 🎯 MAIN API
    public PromptBundle getPrompt(
            String engine,
            String category
    ) {
        Map<String, Object> section =
                store.getSection(engine, category);


        if (section == null) {
            return null;
        }
        return new PromptBundle(section);
    }
}

