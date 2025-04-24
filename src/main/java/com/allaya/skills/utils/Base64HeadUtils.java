package com.allaya.skills.utils;

import com.allaya.skills.AllayaSkills;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class Base64HeadUtils {

    public static ItemStack getHead(String base64) {
        if (base64 == null || base64.isEmpty()) {
            base64 = AllayaSkills.getInstance().getConfig().getString("invalid-head", "");
        }

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", base64));

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);

            head.setItemMeta(meta);
        } catch (Exception e) {
            System.out.println("[Base64HeadUtils] Falha ao aplicar textura base64: " + e.getMessage());
        }

        return head;
    }
}
