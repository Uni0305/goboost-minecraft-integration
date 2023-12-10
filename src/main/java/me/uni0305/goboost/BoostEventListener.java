package me.uni0305.goboost;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BoostEventListener implements Listener {
    public BoostEventListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onReceiveBoostNotification(AsyncReceiveBoostNotificationEvent event) {
        // TODO: Not yet implemented
        BoostIntegrationPlugin.getPlugin().getSLF4JLogger().info("Received boost notification: " + event.getNotifications());
    }
}
