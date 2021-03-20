package edgrrrr.dce.commands.money;

import edgrrrr.dce.DCEPlugin;
import edgrrrr.dce.config.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SendCashTC implements TabCompleter {
    private final DCEPlugin app;

    public SendCashTC(DCEPlugin app) {
        this.app = app;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Ensure player
        if (!(sender instanceof Player) || !(this.app.getConfig().getBoolean(Setting.COMMAND_SEND_CASH_ENABLE_BOOLEAN.path()))) {
            return null;
        }

        String[] strings;
        switch (args.length) {
            // Args 0
            // get player names
            case 0:
                strings = this.app.getPlayerManager().getOfflinePlayers();
                break;

            // Args 1
            // get player names that start with args[0]
            case 1:
                strings = this.app.getPlayerManager().getOfflinePlayers(args[0]);
                break;

            // Args 2
            // just return some numbers
            case 2:
                String balance = String.valueOf(this.app.getEconomyManager().getRoundedBalance((Player) sender));
                strings = new String[]{
                        "1", "10", "100", "1000", balance
                };
                break;

            default:
                strings = new String[0];
                break;
        }

        return Arrays.asList(strings);
    }
}
