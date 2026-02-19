package me.MILLSBOSS.relicLaw;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Random;

/**
 * Makes RelicLaw appear like normal enchanted book trades by occasionally
 * replacing a generated enchanted book offer with a RelicLaw Tome while
 * preserving the original trade costs and properties.
 */
public class LibrarianTradeListener implements Listener {

    private static final Random RNG = new Random();
    private static final double REPLACE_CHANCE = 0.10; // 10% of enchanted book offers become RelicLaw
    private final NamespacedKey silkKey;

    public LibrarianTradeListener(NamespacedKey silkKey) {
        this.silkKey = silkKey;
    }

    @EventHandler
    public void onAcquireTrade(VillagerAcquireTradeEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (villager.getProfession() != Villager.Profession.LIBRARIAN) return;

        MerchantRecipe original = event.getRecipe();
        if (original == null) return;

        // Only consider replacing normal enchanted book trades
        ItemStack originalResult = original.getResult();
        if (originalResult == null || originalResult.getType() != Material.ENCHANTED_BOOK) return;

        // Chance gate so RelicLaw appears among other enchantments, not always
        if (RNG.nextDouble() > REPLACE_CHANCE) return;

        // Build tome result but keep all original trade properties/costs
        ItemStack tome = RelicLawTome.create(silkKey);
        MerchantRecipe replacement = new MerchantRecipe(tome, original.getMaxUses());
        replacement.setIngredients(original.getIngredients());
        replacement.setVillagerExperience(original.getVillagerExperience());
        replacement.setPriceMultiplier(original.getPriceMultiplier());
        replacement.setDemand(original.getDemand());
        try {
            replacement.setIgnoreDiscounts(original.shouldIgnoreDiscounts());
        } catch (Throwable ignored) {
            // API compatibility: method may not exist on some versions
        }

        event.setRecipe(replacement);
    }
}
