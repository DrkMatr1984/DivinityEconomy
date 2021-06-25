package edgrrrr.de.commands;

import edgrrrr.de.DEPlugin;
import edgrrrr.de.config.Setting;
import org.bukkit.entity.Player;

public abstract class DivinityCommandEnchant extends DivinityCommand {
    protected final boolean marketIsEnabled;

    /**
     * Constructor
     *
     * @param app
     * @param registeredCommandName
     * @param hasConsoleSupport
     * @param commandSetting
     */
    public DivinityCommandEnchant(DEPlugin app, String registeredCommandName, boolean hasConsoleSupport, Setting commandSetting) {
        super(app, registeredCommandName, hasConsoleSupport, commandSetting);
        this.marketIsEnabled = this.app.getConfig().getBoolean(Setting.MARKET_ENCHANTS_ENABLE_BOOLEAN.path);
    }

    @Override
    public boolean _onPlayerCommand(Player sender, String[] args) {
        if (!this.marketIsEnabled) {
            this.app.getConsole().send(sender, CommandResponse.EnchantMarketIsDisabled.defaultLogLevel, CommandResponse.EnchantMarketIsDisabled.message);
            return true;
        } else if (!this.isEnabled) {
            this.app.getConsole().send(sender, CommandResponse.PlayerCommandIsDisabled.defaultLogLevel, CommandResponse.PlayerCommandIsDisabled.message);
            return true;
        } else {
            return this.onPlayerCommand(sender, args);
        }
    }

    @Override
    public boolean _onConsoleCommand(String[] args) {
        if (!this.marketIsEnabled) {
            this.app.getConsole().send(CommandResponse.EnchantMarketIsDisabled.defaultLogLevel, CommandResponse.EnchantMarketIsDisabled.message);
            return true;
        } else if (!this.isEnabled) {
            this.app.getConsole().send(CommandResponse.ConsoleCommandIsDisabled.defaultLogLevel, CommandResponse.ConsoleCommandIsDisabled.message);
            return true;
        } else if (!this.hasConsoleSupport) {
            this.app.getConsole().send(CommandResponse.ConsoleSupportNotAdded.defaultLogLevel, CommandResponse.ConsoleSupportNotAdded.message);
            return true;
        } else {
            return this.onConsoleCommand(args);
        }
    }
}
