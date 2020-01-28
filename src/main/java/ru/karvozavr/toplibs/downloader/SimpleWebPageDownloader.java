package ru.karvozavr.toplibs.downloader;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Single-threaded web-page Downloader implementation.
 */
public class SimpleWebPageDownloader implements WebPageDownloader {

    private static final String BROWSER_USER_AGENT_HEADER = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";

    @Override
    @NotNull
    public Page downloadPage(@NotNull String urlString) {
        String rawHtml;
        try {
            rawHtml = retrieveHtml(new URL(urlString));
        } catch (IOException e) {
            throw new PageDownloadFailedError(e);
        }

        return new Page(rawHtml);
    }

    @NotNull
    private String retrieveHtml(URL url) throws IOException {
        String rawHtml = "";
        boolean tryAgain = true;
        while (tryAgain) {
            tryAgain = false;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", BROWSER_USER_AGENT_HEADER);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                rawHtml = readDataFromInputStream(connection.getInputStream());
            } else if (isRedirect(responseCode)) {
                tryAgain = true;
                url = new URL(connection.getHeaderField("Location"));
            } else {
                throw new PageDownloadFailedError(String.format("Connection failed with error code %s", responseCode));
            }
        }
        return rawHtml;
    }

    @NotNull
    private String readDataFromInputStream(@NotNull InputStream inputStream) throws IOException {
        StringBuilder resultBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append('\n');
            }
        }
        return resultBuilder.toString();
    }

    private boolean isRedirect(int responseCode) {
        return responseCode == HttpURLConnection.HTTP_MOVED_PERM
            || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
            || responseCode == HttpURLConnection.HTTP_SEE_OTHER;
    }
}
