package com.allaya.skills.skills;

import java.util.List;

public class SkillData {
    public String id;
    public String name;
    public String description;
    public String path;
    public int unlockLevel;
    public int skillPointsRequired;
    public List<String> requires;
    public List<String> regionAllowed;
    public int cooldown;
    public List<SkillEffect> effects;
}
