package me.MILLSBOSS.relicLaw;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class RelicLawCommand implements CommandExecutor {

    public static final String RELIC_LAW_KEY = "reliclaw";

    private final NamespacedKey key;

    public RelicLawCommand(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("reliclaw.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Hold a pickaxe in your main hand.");
            return true;
        }

        if (!isSupportedPickaxe(item.getType())) {
            player.sendMessage(ChatColor.RED + "Relics Law can only be applied to Netherite pickaxes.");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(ChatColor.RED + "This item cannot be modified.");
            return true;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (Boolean.TRUE.equals(pdc.get(key, PersistentDataType.BOOLEAN))) {
            player.sendMessage(ChatColor.YELLOW + "This pickaxe already has Relics Law.");
            return true;
        }

        int costLevels = 8;
        if (player.getLevel() < costLevels) {
            player.sendMessage(ChatColor.RED + "You need at least " + costLevels + " experience levels.");
            return true;
        }

        // Charge levels
        player.setLevel(player.getLevel() - costLevels);

        // Tag the item and add lore
        pdc.set(key, PersistentDataType.BOOLEAN, true);

        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Relics Law I");
        meta.setLore(lore);

        item.setItemMeta(meta);

        player.sendMessage(ChatColor.GREEN + "Applied Relics Law to your pickaxe for " + costLevels + " levels.");
        return true;
    }

    public static boolean isSupportedPickaxe(Material type) {
        return type == Material.NETHERITE_PICKAXE;
    }
}
