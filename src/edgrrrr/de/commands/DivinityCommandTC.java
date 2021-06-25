package edgrrrr.de.commands;

import edgrrrr.de.DEPlugin;
import edgrrrr.de.commands.DivinityCommand.CommandResponse;
import edgrrrr.de.config.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The default inherited class for all Divinity Command Tab Completer
 */
public abstract class DivinityCommandTC implements TabCompleter {
    // Link to app
    // Whether the command is enabled or not
    // Whether the command supports console input
    protected final DEPlugin app;
    protected final boolean isEnabled;
    protected final boolean hasConsoleSupport;

    /**
     * Constructor
     * @param app
     * @param hasConsoleSupport
     * @param commandSetting
     */
    public DivinityCommandTC(DEPlugin app, boolean hasConsoleSupport, Setting commandSetting) {
        this.app = app;
        this.hasConsoleSupport = hasConsoleSupport;
        this.isEnabled = this.app.getConfig().getBoolean(commandSetting.path);
    }

    /**
     * The command event all user commands call upon send
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player) {
                return this._onPlayerTabComplete((Player) sender, args);
            } else {
                return this._onConsoleTabComplete(args);
            }
        } catch (Exception e) {
            this.app.getConsole().send(CommandResponse.ErrorOnCommand.defaultLogLevel, String.format(CommandResponse.ErrorOnCommand.message, this.getClass().getCanonicalName(), e.getMessage()));
            return null;
        }
    }

    /**
     * The pre-handling of onPlayerCommand
     * Checks the command is enabled
     * @param sender
     * @param args
     * @return
     */
    public List<String> _onPlayerTabComplete(Player sender, String[] args){
        if (!this.isEnabled) {
            return null;
        } else {
            return this.onPlayerTabCompleter(sender, args);
        }
    }

    /**
     * ###To be overridden by the actual command
     * For handling a player calling this command
     * @param sender
     * @param args
     * @return
     */
    public abstract List<String> onPlayerTabCompleter(Player sender, String[] args);

    /**
     * The pre-handling of the onConsoleCommand
     * Checks the command is enabled and has console support
     * @param args
     * @return
     */
    public List<String> _onConsoleTabComplete(String[] args) {
        if (!this.isEnabled) {
            return null;
        } else if (!this.hasConsoleSupport) {
            return null;
        } else {
            return this.onConsoleTabCompleter(args);
        }
    }

    /**
     * ###To be overridden by the actual command
     * For the handling of the console calling this command
     * @param args
     * @return
     */
    public abstract List<String> onConsoleTabCompleter(String[] args);
}
