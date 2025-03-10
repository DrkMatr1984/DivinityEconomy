package me.edgrrrr.de.placeholders.expansions;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.placeholders.DivinityExpansion;
import org.bukkit.OfflinePlayer;

public class PAPIFormatMoney extends DivinityExpansion {
    public PAPIFormatMoney(DEPlugin main) {
        super(main, "^format_money_(.*)$");
    }

    @Override
    public String getResult(OfflinePlayer player, String value) {
        return this.getMain().getConsole().formatMoney(Double.parseDouble(value.replaceFirst(this.value, "$1")));
    }
}
