package edgrrrr.dce.commands.market;

import edgrrrr.dce.DCEPlugin;
import edgrrrr.dce.config.Setting;
import edgrrrr.dce.materials.MaterialData;
import edgrrrr.dce.math.Math;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BuyItemTC implements TabCompleter {
    private final DCEPlugin app;

    public BuyItemTC(DCEPlugin app) {
        this.app = app;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Ensure player
        if (!(sender instanceof Player) || !(this.app.getConfig().getBoolean(Setting.COMMAND_BUY_ITEM_ENABLE_BOOLEAN.path()))) {
            return null;
        }

        String[] strings;
        MaterialData materialData;
        switch (args.length) {
            // 1 args
            // return names of players starting with arg
            case 1:
                strings = this.app.getMaterialManager().getMaterialNames(args[0]);
                break;

            // 2 args
            // return max stack size for the material given
            case 2:
                materialData = this.app.getMaterialManager().getMaterial(args[0]);
                int stackSize = 64;
                if (materialData != null) {
                    stackSize = materialData.getMaterial().getMaxStackSize();
                }

                strings = new String[] {
                    String.valueOf(stackSize)
                };
                break;

            // 3 args
            // If uses clicks space after number, returns the value of the amount of item given
            case 3:
                materialData = this.app.getMaterialManager().getMaterial(args[0]);
                String value = "unknown";
                if (materialData != null) {
                    value = String.format("£%,.2f", this.app.getMaterialManager().calculatePrice(Math.getInt(args[1]), materialData.getQuantity(), this.app.getMaterialManager().materialBuyTax, true));
                }

                strings = new String[] {
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
}
