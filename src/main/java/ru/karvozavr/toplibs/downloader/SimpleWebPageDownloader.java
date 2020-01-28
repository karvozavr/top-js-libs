package ru.karvozavr.toplibs.downloader;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

import static ru.karvozavr.toplibs.util.IOUtil.*;

/**
 * Simple web-page Downloader implementation capable of processing redirects.
 */
public class SimpleWebPageDownloader implements WebPageDownloader {

    @Override
    @NotNull
    public Optional<Page> downloadPage(@NotNull String url) {
        String rawHtml;
        try {
            rawHtml = retrieveHtml(url);
        } catch (IOException | PageDownloadFailedError e) {
            return Optional.empty();
        }

        return Optional.of(new Page(rawHtml));
    }

    @NotNull
    private String retrieveHtml(@NotNull String url) throws IOException {
        String rawHtml = "";
        boolean tryAgain = true;
        while (tryAgain) {
            tryAgain = false;

            HttpURLConnection connection = openHttpConnection(url);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                rawHtml = readDataFromInputStream(connection.getInputStream());
            } else if (isRedirect(responseCode)) {
                tryAgain = true;
                url = connection.getHeaderField("Location");
            } else {
                throw new PageDownloadFailedError(String.format("Connection failed with error code %s", responseCode));
            }
        }
        return rawHtml;
    }
}
