package me.uni0305.goboost.api;

import me.uni0305.goboost.YamlConfigurator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BoostAPI {
    private static final String API_ENDPOINT = "https://integration.goboost.tv";

    public static @NotNull CompletableFuture<@NotNull BoostEventResponse> getEvents(@NotNull String cursor) throws RuntimeException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("%s/v1/integration/events?secret=%s&cursor=%s".formatted(API_ENDPOINT, getSecretKey(), cursor));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BoostEventResponse response = BoostEventResponse.fromJson(reader);
                if (response == null) throw new IllegalStateException("Invalid response from Boost API");
                return response;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static @NotNull String getSecretKey() throws NullPointerException {
        return Objects.requireNonNull(YamlConfigurator.getConfig().getString("boost-api-secret-key"));
    }
}
