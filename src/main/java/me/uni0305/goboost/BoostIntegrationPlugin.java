package me.uni0305.goboost;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BoostIntegrationPlugin extends JavaPlugin {
    public static @NotNull JavaPlugin getPlugin() {
        return getPlugin(BoostIntegrationPlugin.class);
    }

    @Override
    public void onEnable() {
        YamlConfigurator.saveDefaultConfig();
        YamlConfigurator.reloadConfig();
        new BoostCommand(this);
    }
}
