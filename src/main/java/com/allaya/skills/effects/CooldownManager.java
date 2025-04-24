package com.allaya.skills.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isReady(UUID uuid, String skillId) {
        Map<String, Long> map = cooldowns.getOrDefault(uuid, new HashMap<>());
        long now = System.currentTimeMillis();
        return !map.containsKey(skillId) || map.get(skillId) <= now;
    }

    public static void start(UUID uuid, String skillId, int seconds) {
        cooldowns.computeIfAbsent(uuid, k -> new HashMap<>())
                .put(skillId, System.currentTimeMillis() + (seconds * 1000L));
    }
}
