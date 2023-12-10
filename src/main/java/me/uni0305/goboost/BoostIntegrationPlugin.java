package me.uni0305.goboost;

import me.uni0305.goboost.api.BoostAPI;
import me.uni0305.goboost.api.BoostEventResponse;
import me.uni0305.goboost.api.BoostNotificationData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BoostIntegrationPlugin extends JavaPlugin {
    public static @NotNull JavaPlugin getPlugin() {
        return getPlugin(BoostIntegrationPlugin.class);
    }

    @Override
    public void onEnable() {
        YamlConfigurator.saveDefaultConfig();
        YamlConfigurator.reloadConfig();
        new BoostCommand(this);
        new BoostEventListener(this);
        schedulePollingTask();
    }

    private void schedulePollingTask() {
        AtomicReference<String> cursor = new AtomicReference<>("");
        getServer().getAsyncScheduler().runAtFixedRate(this, (task) -> {
            BoostEventResponse response;
            try {
                response = BoostAPI.getEvents(cursor.get()).get();
            } catch (InterruptedException | ExecutionException e) {
                task.cancel();
                getSLF4JLogger().error("An error occurred while fetching boost events", e);
                return;
            }
            if (response == null || !response.isSuccess()) return;

            cursor.set(response.cursor());
            List<@NotNull BoostNotificationData> notifications = response.notifications();
            if (notifications == null || notifications.isEmpty()) return;

            boolean called = new AsyncReceiveBoostNotificationEvent(notifications).callEvent();
            if (!called) {
                task.cancel();
                getSLF4JLogger().error("An error occurred while calling AsyncReceiveBoostNotificationEvent");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
