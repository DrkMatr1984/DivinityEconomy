package me.edgrrrr.de.commands.market;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.commands.DivinityCommandMaterials;
import me.edgrrrr.de.config.Setting;
import me.edgrrrr.de.market.items.materials.MarketableMaterial;
import me.edgrrrr.de.player.PlayerManager;
import me.edgrrrr.de.response.MultiValueResponse;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A command for selling all items to the market
 */
public class SellAll extends DivinityCommandMaterials {

    /**
     * Constructor
     *
     * @param app
     */
    public SellAll(DEPlugin app) {
        super(app, "sellall", false, Setting.COMMAND_SELLALL_ITEM_ENABLE_BOOLEAN);
    }

    /**
     * For handling a player calling this command
     *
     * @param sender
     * @param args
     * @return
     */
    @Override
    public boolean onPlayerCommand(Player sender, String[] args) {
        // Whether the material names are items to sell or blocked materials
        boolean blocking = false;
        // The material data
        Set<MarketableMaterial> marketableMaterials = new HashSet<>();

        switch (args.length) {
            case 0:
                break;

            // with specifying args
            case 1:
                String arg = args[0];
                if (arg.startsWith("!")) {
                    blocking = true;
                    arg = arg.replaceFirst("!", "");
                }
                for (String materialName : arg.split(",")) {
                    MarketableMaterial marketableMaterial = this.getMain().getMarkMan().getItem(materialName);
                    if (marketableMaterial == null) {
                        this.getMain().getConsole().send(sender, CommandResponse.InvalidItemName.defaultLogLevel, CommandResponse.InvalidItemName.message, materialName);
                        return true;
                    } else {
                        marketableMaterials.add(marketableMaterial);
                    }
                }
                break;

            default:
                this.getMain().getConsole().usage(sender, CommandResponse.InvalidNumberOfArguments.message, this.help.getUsages());
                return true;
        }

        // Get player inventory
        // Copy all inventory items over to itemStacks that are either specified or blocked
        ItemStack[] playerInventory = PlayerManager.getInventoryMaterials(sender);
        ArrayList<ItemStack> itemStackList = new ArrayList<>();
        for (ItemStack itemStack : playerInventory) {
            MarketableMaterial marketableMaterial = this.getMain().getMarkMan().getItem(itemStack);
            if ((blocking && !marketableMaterials.contains(marketableMaterial)) || (!blocking && marketableMaterials.contains(marketableMaterial)) || (!blocking && marketableMaterials.size() == 0)) {
                itemStackList.add(itemStack);
            }
        }

        // Get item stacks
        // Clone incase need to be refunded
        // Get valuation
        ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
        ItemStack[] itemStacksClone = MarketableMaterial.cloneItems(itemStacks);
        MultiValueResponse response = this.getMain().getMarkMan().getBulkSellValue(itemStacks);

        if (response.isSuccess()) {
            PlayerManager.removePlayerItems(itemStacks);

            EconomyResponse economyResponse = this.getMain().getEconMan().addCash(sender, response.getTotalValue());
            if (!economyResponse.transactionSuccess()) {
                PlayerManager.addPlayerItems(sender, itemStacksClone);
                // Handles console, player message and mail
                this.getMain().getConsole().logFailedSale(sender, response.getTotalQuantity(), "items", economyResponse.errorMessage);
            } else {
                for (String string: response.quantities.keySet()) {
                    MarketableMaterial material = this.getMain().getMarkMan().getItem(string);
                    material.getManager().editQuantity(material, response.quantities.get(material.getID()));
                }

                // Handles console, player message and mail
                this.getMain().getConsole().logSale(sender, response.getTotalQuantity(), response.getTotalValue(), "items");
            }
        } else {
            // Handles console, player message and mail
            this.getMain().getConsole().logFailedSale(sender, response.getTotalQuantity(), "items", response.errorMessage);
        }

        return true;
    }

    /**
     * For the handling of the console calling this command
     *
     * @param args
     * @return
     */
    @Override
    public boolean onConsoleCommand(String[] args) {
        return false;
    }
}
