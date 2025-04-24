package com.allaya.skills.effects;

import java.util.HashMap;
import java.util.Map;

public class EffectRegistry {

    private static final Map<String, EffectExecutor> executors = new HashMap<>();

    public static void register(String id, EffectExecutor executor) {
        executors.put(id.toUpperCase(), executor);
    }

    public static EffectExecutor getExecutor(String id) {
        return executors.get(id.toUpperCase());
    }
}
