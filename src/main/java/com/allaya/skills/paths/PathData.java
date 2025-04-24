package com.allaya.skills.paths;

import java.util.HashMap;
import java.util.Map;

public class PathData {

    public final String id;
    public final String displayName;
    public final String xpSource;
    public final int xpPerEvent;
    public final int startingXp;
    public final int xpIncreasePerLevel;
    public final int pointsPerLevel;
    public final Map<String, Integer> blockXp;

    public PathData(String id, String displayName, String xpSource, int xpPerEvent,
                    int startingXp, int xpIncreasePerLevel, int pointsPerLevel,
                    Map<String, Integer> blockXp) {
        this.id = id;
        this.displayName = displayName;
        this.xpSource = xpSource;
        this.xpPerEvent = xpPerEvent;
        this.startingXp = startingXp;
        this.xpIncreasePerLevel = xpIncreasePerLevel;
        this.pointsPerLevel = pointsPerLevel;
        this.blockXp = blockXp;
    }

    public int getXpForBlock(String material) {
        return blockXp.getOrDefault(material.toUpperCase(), xpPerEvent);
    }

    public int getXpForLevel(int level) {
        return startingXp + (level - 1) * xpIncreasePerLevel;
    }

    public int getStartingXp() {
        return startingXp;
    }
}
