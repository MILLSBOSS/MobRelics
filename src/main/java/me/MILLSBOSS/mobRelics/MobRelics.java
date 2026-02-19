package me.MILLSBOSS.mobRelics;

import me.MILLSBOSS.eggDrops.DropListener;
import me.MILLSBOSS.eggDrops.PluginConfig;
import me.MILLSBOSS.eggDrops.SpawnerSpawnListener;
import me.MILLSBOSS.relicLaw.AnvilSilkListener;
import me.MILLSBOSS.relicLaw.LibrarianTradeListener;
import me.MILLSBOSS.relicLaw.RelicLawCommand;
import me.MILLSBOSS.relicLaw.SpawnerSilkListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class MobRelics extends JavaPlugin {

    private NamespacedKey silkKey;
    private final Random random = new Random();

    @Override
    public void onEnable() {
        // Key for RelicLaw item tagging
        silkKey = new NamespacedKey(this, RelicLawCommand.RELIC_LAW_KEY);

        // Register RelicLaw listeners (existing functionality)
        getServer().getPluginManager().registerEvents(new SpawnerSilkListener(silkKey), this);
        getServer().getPluginManager().registerEvents(new LibrarianTradeListener(silkKey), this);
        getServer().getPluginManager().registerEvents(new AnvilSilkListener(silkKey), this);
        // Allow removing RelicLaw via grindstone like normal enchantments
        getServer().getPluginManager().registerEvents(new me.MILLSBOSS.relicLaw.GrindstoneSilkRemovalListener(silkKey), this);

        // Initialize and register EggDrops functionality
        saveDefaultConfig();
        PluginConfig.initDefaults(this);
        getServer().getPluginManager().registerEvents(new DropListener(this, random), this);
        // Tag mobs that originate from spawners so egg drop chance applies equally
        getServer().getPluginManager().registerEvents(new SpawnerSpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        // Nothing to cleanup
    }
}
