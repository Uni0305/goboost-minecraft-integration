package me.uni0305.goboost;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YamlConfigurator {
    private static final String filename = "config.yml";
    private static final JavaPlugin plugin = BoostIntegrationPlugin.getPlugin();

    private static @Nullable File file = null;
    private static @Nullable FileConfiguration config = null;

    public static void reloadConfig() {
        if (file == null) file = new File(plugin.getDataFolder(), filename);
        config = YamlConfiguration.loadConfiguration(file);

        InputStream resource = plugin.getResource(filename);
        if (resource == null) return;
        InputStreamReader reader = new InputStreamReader(resource);
        YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
        config.setDefaults(defaults);
    }

    public static @NotNull FileConfiguration getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

    public static void saveConfig() {
        if (file == null || config == null) return;
        try {
            getConfig().save(file);
            plugin.getSLF4JLogger().debug("Saved config to " + file);
        } catch (IOException e) {
            plugin.getSLF4JLogger().error("Could not save config to " + file, e);
        }
    }

    public static void saveDefaultConfig() {
        if (file == null) file = new File(plugin.getDataFolder(), filename);
        if (file.exists()) return;
        plugin.saveResource(filename, false);
        plugin.getSLF4JLogger().debug("Saved default config to " + filename);
    }
}
