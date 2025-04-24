package com.allaya.skills.paths;

public class PlayerPathProgress {
    public int level;
    public int currentXp;
    public int xpNeeded;
    public int pointsEarned;
    public int pointsUsed;

    public PlayerPathProgress(int level, int currentXp, int xpNeeded, int pointsEarned, int pointsUsed) {
        this.level = level;
        this.currentXp = currentXp;
        this.xpNeeded = xpNeeded;
        this.pointsEarned = pointsEarned;
        this.pointsUsed = pointsUsed;
    }

    public boolean hasAvailablePoints() {
        return pointsEarned > pointsUsed;
    }

    public void addPoint() {
        pointsEarned++;
    }

    public void usePoint() {
        pointsUsed++;
    }

    public int getAvailablePoints() {
        return pointsEarned - pointsUsed;
    }

}
