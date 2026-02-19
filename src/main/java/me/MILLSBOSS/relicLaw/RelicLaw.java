package me.MILLSBOSS.relicLaw;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class RelicLaw extends JavaPlugin {

    private NamespacedKey silkKey;

    @Override
    public void onEnable() {
        silkKey = new NamespacedKey(this, RelicLawCommand.RELIC_LAW_KEY);
        // Register listeners (anvil-only application)
        getServer().getPluginManager().registerEvents(new SpawnerSilkListener(silkKey), this);
        getServer().getPluginManager().registerEvents(new LibrarianTradeListener(silkKey), this);
        getServer().getPluginManager().registerEvents(new AnvilSilkListener(silkKey), this);
    }

    @Override
    public void onDisable() {
        // Nothing to cleanup
    }
}
