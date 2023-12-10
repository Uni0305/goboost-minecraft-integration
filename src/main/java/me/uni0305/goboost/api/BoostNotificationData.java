package me.uni0305.goboost.api;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

public record BoostNotificationData(
        @SerializedName("Id") @NotNull String id,
        @SerializedName("UserId") @NotNull String userId,
        @SerializedName("UserName") @NotNull String userName,
        @SerializedName("Currency") @NotNull String currency,
        @SerializedName("Amount") long amount,
        @SerializedName("CreatedAt") @NotNull String createdAt
) {
    public @NotNull ZonedDateTime getCreatedDateTime() {
        return ZonedDateTime.parse(createdAt);
    }
}
