package me.edgrrrr.de.market.items.materials.block;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.config.Setting;
import me.edgrrrr.de.market.items.materials.MarketableMaterial;
import me.edgrrrr.de.market.items.materials.MaterialManager;
import me.edgrrrr.de.response.ValueResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.concurrent.ConcurrentHashMap;

public class BlockManager extends MaterialManager {
    // Stores the default items.json file location
    private static final String materialsFile = "materials.yml";
    private static final String aliasesFile = "materialAliases.yml";

    // Other settings
    private boolean itemDmgScaling = false;

    /**
     * Constructor You will likely need to call loadMaterials and loadAliases to
     * populate the aliases and materials with data from the program
     *
     * @param main - The plugin
     */
    public BlockManager(DEPlugin main) {
        super(main, materialsFile, aliasesFile, new ConcurrentHashMap<String, MarketableBlock>());
    }

    /**
     * Returns the scaling of price for an item, based on its durability and damage.
     *
     * @param itemStack - The itemstack containing the material with the specified damage.
     * @return double - The level of price scaling to apply. For example .9 = 90% of full price. Maximum value is 1 for undamaged.
     */
    private static double getDamageValue(ItemStack itemStack) {
        // Instantiate damage value
        double damageValue = 1.0;

        // Get meta and cast to damageable, for getting the items durability
        // Get durability and max durability
        Damageable dmg = (Damageable) itemStack.getItemMeta();
        if (dmg == null) return damageValue;
        double durability = dmg.getDamage();
        double maxDurability = itemStack.getType().getMaxDurability();

        // If max durability > 0 - Meaning the item is damageable (aka a tool)
        // Adjust damage value to be the percentage of health left on the item.
        // 50% damaged = .5 scaling (50% of full price)
        // Durability is in the form of 1 = 1 damage (if item has 10 health, 1 durability = 9 health)
        // Hence maxDurability - durability / maxDurability
        if (maxDurability > 0) {
            damageValue = (maxDurability - durability) / maxDurability;
        }

        return damageValue;
    }

    public MarketableBlock getMaterial(String alias) {
        return (MarketableBlock) this.getItem(alias);
    }

    /**
     * Called by init
     */
    @Override
    public void init() {
        super.init();
        this.itemDmgScaling = this.getConfMan().getBoolean(Setting.MARKET_MATERIALS_ITEM_DMG_SCALING_BOOLEAN);
    }

    @Override
    public String getType() {
        return "MATERIAL";
    }

    /**
     * Returns the damage scaling of the item given
     *
     * @param itemStack
     * @return
     */
    private double getDamageScaling(ItemStack itemStack) {
        if (this.itemDmgScaling) {
            return BlockManager.getDamageValue(itemStack);
        } else {
            return 1.0;
        }
    }

    /**
     * Returns the sell value for a single stack of items.
     *
     * @param itemStack - The itemStack to get the value of
     * @return MaterialValue - The price of the itemstack if no errors occurred.
     */
    @Override
    public ValueResponse getSellValue(ItemStack itemStack, int amount) {
        ValueResponse response;

        if (this.getEnchMan().isEnchanted(itemStack)) {
            response = new ValueResponse(0.0, ResponseType.FAILURE, "item is enchanted.");

        } else {
            MarketableBlock materialData = (MarketableBlock) this.getItem(itemStack);

            if (materialData == null) {
                response = new ValueResponse(0.0, ResponseType.FAILURE, "item cannot be found.");
            } else {
                if (!materialData.getAllowed()) {
                    response = new ValueResponse(0.0, ResponseType.FAILURE, "item is banned.");
                } else {
                    double value = this.calculatePrice(amount, materialData.getQuantity(), (this.sellScale * this.getDamageScaling(itemStack)), false);
                    if (value > 0) {
                        response = new ValueResponse(value, ResponseType.SUCCESS, "");
                    } else {
                        response = new ValueResponse(0.0, ResponseType.FAILURE, "market is saturated.");
                    }
                }
            }
        }

        return response;
    }

    /**
     * Returns the value of an itemstack
     *
     * @param itemStack - The item stack to get the value of
     * @return MaterialValue
     */
    @Override
    public ValueResponse getBuyValue(ItemStack itemStack, int amount) {
        ValueResponse response;

        MarketableBlock materialData = (MarketableBlock) this.getItem(itemStack);
        if (materialData == null) {
            response = new ValueResponse(0.0, ResponseType.FAILURE, "item cannot be found.");
        } else {
            if (!materialData.getAllowed()) {
                response = new ValueResponse(0.0, ResponseType.FAILURE, "item is banned.");
            } else {
                double value = this.calculatePrice(amount, materialData.getQuantity(), this.buyScale, true);
                if (value > 0) {
                    response = new ValueResponse(value, ResponseType.SUCCESS, "");
                } else {
                    response = new ValueResponse(0.0, ResponseType.FAILURE, "market is saturated.");
                }
            }
        }

        return response;
    }

    @Override
    public MarketableMaterial loadItem(String ID, ConfigurationSection data, ConfigurationSection defaultData) {
        return new MarketableBlock(this.getMain(), this, ID, data, defaultData);
    }
}
