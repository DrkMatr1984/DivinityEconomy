package me.edgrrrr.de.commands.market;

import me.edgrrrr.de.DEPlugin;
import me.edgrrrr.de.commands.DivinityCommandMaterialsTC;
import me.edgrrrr.de.config.Setting;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * tab completer for the info command
 */
public class InfoTC extends DivinityCommandMaterialsTC {

    /**
     * Constructor
     *
     * @param app
     */
    public InfoTC(DEPlugin app) {
        super(app, "information", true, Setting.COMMAND_INFO_ENABLE_BOOLEAN);
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
        return this.onConsoleTabCompleter(args);
    }

    /**
     * For the handling of the console calling this command
     *
     * @param args
     * @return
     */
    @Override
    public List<String> onConsoleTabCompleter(String[] args) {
        String[] strings;
        switch (args.length) {
            // 1 args
            // return names of players starting with arg
            case 1:
                strings = this.getMain().getMarkMan().getItemNames(args[0]);
                break;

            // else
            default:
                strings = new String[0];
                break;
        }

        return Arrays.asList(strings);
    }
}
