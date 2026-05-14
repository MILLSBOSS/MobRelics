package me.MILLSBOSS.eggDrops;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Tags mobs that originate from spawners so they can be handled with the same
 * spawn egg drop chance as naturally spawned ones, even if killed indirectly.
 */
public class SpawnerSpawnListener implements Listener {

    public static final String SPAWNER_TAG_KEY = "mobrelics_spawner";
    private final NamespacedKey spawnerKey;
    private final JavaPlugin plugin;

    public SpawnerSpawnListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.spawnerKey = new NamespacedKey(plugin, SPAWNER_TAG_KEY);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            event.getEntity().getPersistentDataContainer().set(spawnerKey, PersistentDataType.BYTE, (byte) 1);
        }
    }

    public NamespacedKey getSpawnerKey() {
        return spawnerKey;
    }
}
