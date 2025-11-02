package me.MILLSBOSS.eggDrops;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public final class PluginConfig {

    // Define Nether and End entity sets. Everything else defaults to Overworld.
    private static final Set<EntityType> NETHER = EnumSet.of(
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.MAGMA_CUBE,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.HOGLIN,
            EntityType.ZOGLIN,
            EntityType.STRIDER,
            EntityType.WITHER_SKELETON
    );

    private static final Set<EntityType> END = EnumSet.of(
            EntityType.ENDERMAN,
            EntityType.SHULKER,
            EntityType.ENDERMITE
    );

    private PluginConfig() {}

    public static void initDefaults(JavaPlugin plugin) {
        FileConfiguration cfg = plugin.getConfig();
        // Ensure root sections exist
        ensureSection(cfg, "overworld");
        ensureSection(cfg, "nether");
        ensureSection(cfg, "end");

        for (EntityType type : EntityType.values()) {
            if (!type.isAlive()) continue; // only living mobs
            if (!type.isSpawnable()) continue; // ignore technical types
            if (materialFor(type) == null) continue; // only entities with spawn eggs

            String category = categoryFor(type);
            String path = category + "." + type.name();
            if (!cfg.isSet(path)) {
                cfg.set(path, 0.0D); // default 0% chance (disabled)
            }
        }
        plugin.saveConfig();
    }

    public static String categoryFor(EntityType type) {
        if (END.contains(type)) return "end";
        if (NETHER.contains(type)) return "nether";
        return "overworld";
    }

    private static void ensureSection(FileConfiguration cfg, String section) {
        if (!cfg.isConfigurationSection(section)) {
            cfg.createSection(section);
        }
    }

    private static Material materialFor(EntityType type) {
        String name = type.name().toUpperCase(Locale.ROOT) + "_SPAWN_EGG";
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
