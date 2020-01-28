package ru.karvozavr.toplibs.util;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IOUtil {

    public static final String BROWSER_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

    public static boolean isRedirect(int responseCode) {
        return responseCode == HttpURLConnection.HTTP_MOVED_PERM
            || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
            || responseCode == HttpURLConnection.HTTP_SEE_OTHER;
    }

    @NotNull
    public static HttpURLConnection openHttpConnection(@NotNull String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", BROWSER_USER_AGENT_HEADER);
        return connection;
    }

    @NotNull
    public static String readDataFromInputStream(@NotNull InputStream inputStream) throws IOException {
        StringBuilder resultBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line).append('\n');
            }
        }
        return resultBuilder.toString();
    }
}
