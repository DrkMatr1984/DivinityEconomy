package me.edgrrrr.de.commands.help;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.commands.DivinityCommand;
import me.edgrrrr.de.config.Setting;
import me.edgrrrr.de.help.Help;
import me.edgrrrr.de.utils.Converter;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * A command for getting help
 */
public class HelpCommand extends DivinityCommand {

    /**
     * Constructor
     *
     * @param app
     */
    public HelpCommand(DEPlugin app) {
        super(app, "ehelp", true, Setting.COMMAND_EHELP_ENABLE_BOOLEAN);
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
        Help help = null;
        int pageNumber = -1;

        switch (args.length) {
            case 0:
                pageNumber = 0;
                break;

            case 1:
                pageNumber = Converter.getInt(args[0]);
                help = this.getMain().getHelpMan().get(args[0]);
                break;

            default:
                this.getMain().getConsole().usage(sender, CommandResponse.InvalidNumberOfArguments.message, this.help.getUsages());
                break;
        }

        Map<Integer, Help[]> helpPages = this.getMain().getHelpMan().getPages(8);
        if (help == null && !helpPages.containsKey(pageNumber - 1)) {
            this.getMain().getConsole().usage(sender, "invalid command or page number", this.help.getUsages());

        } else {
            int maxLength = 30;
            String string;
            if (helpPages.containsKey(pageNumber - 1)) {
                this.getMain().getConsole().info(sender, "Help page %s/%s", pageNumber, helpPages.size());
                for (Help helpCom : helpPages.get(pageNumber - 1)) {
                    this.getMain().getConsole().info(sender, "%s: %s...", helpCom.getCommand(), helpCom.getDescription(20));
                }

            } else {
                this.getMain().getConsole().help(sender, help.getCommand(), help.getDescription(), help.getUsages(), help.getAliases());
            }
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
        return this.onPlayerCommand(null, args);
    }
}
