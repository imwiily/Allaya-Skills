package com.allaya.skills.skills;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

public class PlayerSkillData {

    private final Set<String> unlockedSkills = new HashSet<>();

    public boolean isUnlocked(String id) {
        return unlockedSkills.contains(id.toLowerCase());
    }

    public void unlock(String id) {
        unlockedSkills.add(id.toLowerCase());
    }

    public Set<String> getUnlocked() {
        return unlockedSkills;
    }

    public Set<String> getUnlockedIds() {
        return unlockedSkills;
    }

    public void setUnlocked(Collection<String> skills) {
        unlockedSkills.clear();
        unlockedSkills.addAll(skills);
    }

}
