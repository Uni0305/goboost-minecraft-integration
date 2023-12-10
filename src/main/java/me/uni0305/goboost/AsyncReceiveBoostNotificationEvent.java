package me.uni0305.goboost;

import me.uni0305.goboost.api.BoostNotificationData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AsyncReceiveBoostNotificationEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final List<BoostNotificationData> notifications;

    public AsyncReceiveBoostNotificationEvent(List<BoostNotificationData> notifications) {
        super(true);
        this.notifications = notifications;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public List<BoostNotificationData> getNotifications() {
        return notifications;
    }
}
