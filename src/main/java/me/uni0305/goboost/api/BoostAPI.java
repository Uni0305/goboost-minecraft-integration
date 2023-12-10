package me.uni0305.goboost.api;

import me.uni0305.goboost.BoostIntegrationPlugin;
import me.uni0305.goboost.YamlConfigurator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class BoostAPI {
    private static final @NotNull Logger LOGGER = BoostIntegrationPlugin.getPlugin().getSLF4JLogger();
    private static final @NotNull String API_ENDPOINT = "https://integration.goboost.tv";

    @SuppressWarnings("ExtractMethodRecommender")
    public static @NotNull CompletableFuture<@Nullable BoostEventResponse> getEvents(@NotNull String cursor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String secretKey = getSecretKey();
                if (secretKey == null) throw new NullPointerException("Boost API secret key is not set");

                URL url = new URL("%s/v1/integration/events?secret=%s&cursor=%s".formatted(API_ENDPOINT, secretKey, cursor));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BoostEventResponse response = BoostEventResponse.fromJson(reader);
                if (response == null) throw new RuntimeException("Not a valid boost event response");
                else if (!response.isSuccess()) throw new RuntimeException("Boost event response is not successful: " + response.status());
                else return response;
            } catch (IOException e) {
                LOGGER.error("An error occurred while fetching boost events", e);
            }
            return null;
        });
    }

    private static @Nullable String getSecretKey() {
        return YamlConfigurator.getConfig().getString("boost-api-secret-key");
    }
}
