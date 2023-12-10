package me.uni0305.goboost;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.uni0305.goboost.api.BoostAPI;
import me.uni0305.goboost.api.BoostEventResponse;
import me.uni0305.goboost.api.BoostNotificationData;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PollingTaskScheduler {
    private static final @NotNull AtomicReference<@NotNull String> cursorRef = new AtomicReference<>("");
    private static @Nullable ScheduledTask scheduledTask = null;

    /**
     * Returns whether the task is running or not.
     * @return true if the task is running, false otherwise.
     */
    public static boolean isRunning() {
        return scheduledTask != null && scheduledTask.getExecutionState() == ScheduledTask.ExecutionState.RUNNING;
    }

    /**
     * Starts the task.
     * @param plugin The plugin instance.
     */
    public static void runTask(@NotNull JavaPlugin plugin) {
        scheduledTask = plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, (task) -> {
            boolean exitCode = runTaskConsumer(plugin);
            if (!exitCode) task.cancel();
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Cancels the task.
     */
    public static void cancelTask() {
        if (scheduledTask == null) return;
        scheduledTask.cancel();
    }

    /**
     * Runs the task consumer.
     * @param plugin The plugin instance.
     * @return true if the task is successful, false otherwise.
     */
    private static boolean runTaskConsumer(JavaPlugin plugin) {
        BoostEventResponse response;
        try {
            response = BoostAPI.getEvents(cursorRef.get()).get();
        } catch (InterruptedException | ExecutionException e) {
            plugin.getSLF4JLogger().error("An error occurred while fetching boost events", e);
            return false;
        }
        if (response == null || !response.isSuccess()) return true;

        cursorRef.set(Objects.requireNonNull(response.cursor()));
        List<@NotNull BoostNotificationData> notifications = response.notifications();
        if (notifications == null || notifications.isEmpty()) return true;

        boolean called = new AsyncReceiveBoostNotificationEvent(notifications).callEvent();
        if (!called) {
            plugin.getSLF4JLogger().error("An error occurred while calling AsyncReceiveBoostNotificationEvent");
            return false;
        }
        return true;
    }
}
