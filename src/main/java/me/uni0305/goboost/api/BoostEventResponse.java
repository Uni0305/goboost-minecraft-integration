package me.uni0305.goboost.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.List;

public record BoostEventResponse(@SerializedName("ErrorCode") @NotNull String errorCode,
                                 @SerializedName("Cursor") @NotNull String cursor,
                                 @SerializedName("Notifications") @NotNull List<@NotNull BoostNotificationData> notifications) {
    private static final Gson GSON = new Gson();
    private static final TypeToken<BoostEventResponse> TYPE_TOKEN = new TypeToken<>() {
    };

    public static @Nullable BoostEventResponse fromJson(@NotNull Reader reader) {
        return GSON.fromJson(reader, TYPE_TOKEN.getType());
    }

    public @NotNull String toJson() {
        return GSON.toJson(this, TYPE_TOKEN.getType());
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}