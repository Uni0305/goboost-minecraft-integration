package me.uni0305.goboost;

import me.uni0305.goboost.api.BoostAPI;
import me.uni0305.goboost.api.BoostNotificationData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BoostIntegrationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        YamlConfigurator.saveDefaultConfig();
        YamlConfigurator.reloadConfig();
        new BoostEventListener(this);
        schedulePollingTask();
    }

    private void schedulePollingTask() {
        AtomicReference<String> cursor = new AtomicReference<>("");
        getServer().getAsyncScheduler().runAtFixedRate(this, (task) -> BoostAPI.getEvents(cursor.get()).thenAccept((response) -> {
            cursor.set(response.cursor());
            List<BoostNotificationData> notifications = response.notifications();

            if (notifications.isEmpty()) return;
            boolean called = new AsyncReceiveBoostNotificationEvent(notifications).callEvent();
            if (!called) getSLF4JLogger().error("An error occurred while calling the BoostNotificationEvent");
        }).exceptionally((e) -> {
            getSLF4JLogger().error("An error occurred while polling for boost notifications", e);
            return null;
        }), 0, 1, TimeUnit.SECONDS);
    }

    public static @NotNull JavaPlugin getPlugin() {
        return getPlugin(BoostIntegrationPlugin.class);
    }
}
