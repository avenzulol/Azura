package me.avenzu.azura.util.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.avenzu.azura.Azura;

public class AzuraCommand implements CommandExecutor {

    private final FileConfiguration config = Azura.INST.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("azura")) {
            if (args.length == 2 && args[0].equalsIgnoreCase("autoban")) {
                boolean currentAutobanStatus = config.getBoolean("settings.autoban", true);

                if (args[1].equalsIgnoreCase("true")) {
                    if (currentAutobanStatus) {
                        sender.sendMessage("§cAzura autoban has already been §lenabled§c.");
                    } else {
                        config.set("settings.autoban", true);
                        Azura.INST.saveConfig();
                        sender.sendMessage("§aAzura autoban has been §lenabled§a.");
                    }
                } else if (args[1].equalsIgnoreCase("false")) {
                    if (!currentAutobanStatus) {
                        sender.sendMessage("§cAzura autoban has already been §ldisabled§c.");
                    } else {
                        config.set("settings.autoban", false);
                        Azura.INST.saveConfig();
                        sender.sendMessage("§cAzura autoban has been §ldisabled§c.");
                    }
                } else {
                    sender.sendMessage("§cInvalid argument. Use /azura autoban true|false");
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isAutobanEnabled() {
        return Azura.INST.getConfig().getBoolean("settings.autoban", true);
    }
}
