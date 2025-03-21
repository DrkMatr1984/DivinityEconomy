package me.edgrrrr.de.commands.enchants;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.commands.DivinityCommandEnchantTC;
import me.edgrrrr.de.config.Setting;
import me.edgrrrr.de.market.items.enchants.MarketableEnchant;
import me.edgrrrr.de.player.PlayerManager;
import me.edgrrrr.de.utils.ArrayUtils;
import me.edgrrrr.de.utils.Converter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A tab completer for the enchant hand sell command
 */
public class EnchantHandSellTC extends DivinityCommandEnchantTC {

    /**
     * Constructor
     *
     * @param app
     */
    public EnchantHandSellTC(DEPlugin app) {
        super(app, "esell", false, Setting.COMMAND_E_SELL_ENABLE_BOOLEAN);
    }

    /**
     * For handling a player calling this command
     *
     * @param sender
     * @param args
     * @return
     */
    @Override
    public List<String> onPlayerTabCompleter(Player sender, String[] args) {
        String[] strings;
        MarketableEnchant enchantData;
        ItemStack heldItem = PlayerManager.getHeldItem(sender, new ItemStack(Material.AIR, 0));
        switch (args.length) {
            // 1 args
            // return names of players starting with arg
            case 1:
                ArrayList<String> allStrings = new ArrayList<>();
                allStrings.add("max");
                allStrings.addAll(Arrays.asList(this.getMain().getEnchMan().getItemNames(heldItem, args[0])));
                strings = allStrings.toArray(new String[0]);
                break;

            // 2 args
            // return max stack size for the material given
            case 2:
                enchantData = this.getMain().getEnchMan().getEnchant(args[0]);
                int maxLevel = 1;
                if (enchantData != null) {
                    maxLevel = heldItem.getEnchantmentLevel(enchantData.getEnchantment());
                }

                strings = ArrayUtils.strRange(1, maxLevel);
                break;

            // 3 args
            // If uses clicks space after number, returns the value of the amount of item given
            case 3:
                enchantData = this.getMain().getEnchMan().getEnchant(args[0]);
                String value = "unknown";
                if (enchantData != null) {
                    int ui = heldItem.getEnchantmentLevel(enchantData.getEnchantment());
                    value = String.format("£%,.2f", this.getMain().getEnchMan().calculatePrice(MarketableEnchant.levelsToBooks(ui, ui - Converter.getInt(args[1])), enchantData.getQuantity(), this.getMain().getEnchMan().getSellScale(), false));
                }

                strings = new String[]{
                        String.format("Value: %s", value)
                };
                break;

            // else
            default:
                strings = new String[0];
                break;
        }

        return Arrays.asList(strings);
    }

    /**
     * For the handling of the console calling this command
     *
     * @param args
     * @return
     */
    @Override
    public List<String> onConsoleTabCompleter(String[] args) {
        return null;
    }
}
