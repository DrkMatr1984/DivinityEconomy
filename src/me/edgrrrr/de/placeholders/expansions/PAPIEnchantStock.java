package me.edgrrrr.de.placeholders.expansions;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.market.items.enchants.MarketableEnchant;
import me.edgrrrr.de.placeholders.DivinityExpansion;
import org.bukkit.OfflinePlayer;

public class PAPIEnchantStock extends DivinityExpansion {
    public PAPIEnchantStock(DEPlugin main) {
        super(main, "^enchant_stock_(.*)$");
    }

    @Override
    public String getResult(OfflinePlayer player, String value) {
        String enchant = value.replaceFirst(this.value, "$1");
        MarketableEnchant enchantData = this.getMain().getEnchMan().getEnchant(enchant);
        if (enchantData != null) return String.format("%d", enchantData.getQuantity());
        else return String.format("Unknown enchant '%s'", enchant);
    }
}
