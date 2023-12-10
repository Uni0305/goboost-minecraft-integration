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
    private static final @NotNull String FILENAME = "config.yml";
    private static final @NotNull JavaPlugin PLUGIN = BoostIntegrationPlugin.getPlugin();

    private static @Nullable File file = null;
    private static @Nullable FileConfiguration config = null;

    public static void reloadConfig() {
        if (file == null) file = new File(PLUGIN.getDataFolder(), FILENAME);
        config = YamlConfiguration.loadConfiguration(file);

        InputStream resource = PLUGIN.getResource(FILENAME);
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
            PLUGIN.getSLF4JLogger().debug("Saved config to " + file);
        } catch (IOException e) {
            PLUGIN.getSLF4JLogger().error("Could not save config to " + file, e);
        }
    }

    public static void saveDefaultConfig() {
        if (file == null) file = new File(PLUGIN.getDataFolder(), FILENAME);
        if (file.exists()) return;
        PLUGIN.saveResource(FILENAME, false);
        PLUGIN.getSLF4JLogger().debug("Saved default config to " + FILENAME);
    }
}
