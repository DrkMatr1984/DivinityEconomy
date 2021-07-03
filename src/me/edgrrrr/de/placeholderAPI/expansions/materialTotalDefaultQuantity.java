package me.edgrrrr.de.placeholderAPI.expansions;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.placeholderAPI.DivinityExpansion;
import org.bukkit.OfflinePlayer;

public class materialTotalDefaultQuantity extends DivinityExpansion {
    public materialTotalDefaultQuantity(DEPlugin main) {
        super(main, "^material_total_default_quantity$");
    }

    @Override
    public String getResult(OfflinePlayer player, String value) {
        return String.format("%d", this.getMain().getMaterialManager().getDefaultTotalMaterials());
    }
}
