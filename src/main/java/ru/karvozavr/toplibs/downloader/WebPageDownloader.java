package ru.karvozavr.toplibs.downloader;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;

public interface WebPageDownloader {

    /**
     * Retrieves a page by the given URL.
     *
     * @param url URL to retrieve
     * @return the downloaded page
     */
    @NotNull
    Page downloadPage(@NotNull String url);
}
