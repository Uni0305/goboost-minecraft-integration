package me.uni0305.goboost;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoostCommand extends BukkitCommand {
    public BoostCommand(@NotNull JavaPlugin plugin) {
        super("goboost");
        this.setPermission("op");
        plugin.getServer().getCommandMap().register("goboost", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendRichMessage("<yellow>Usage: </yellow>/%s reload".formatted(label));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            try {
                YamlConfigurator.saveDefaultConfig();
                YamlConfigurator.reloadConfig();
                if (!PollingTaskScheduler.isRunning()) PollingTaskScheduler.runTask(BoostIntegrationPlugin.getPlugin());
                sender.sendRichMessage("<green>Successfully reloaded config.</green>");
            } catch (Exception e) {
                sender.sendRichMessage("<red>An error occurred while reloading config. Please check console for more details.</red>");
                return false;
            }
            return true;
        } else sender.sendRichMessage("<red>Invalid argument: %s</red>".formatted(args[0]));
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 0 || args[0].isBlank()) return List.of("reload");
        return List.of();
    }
}
