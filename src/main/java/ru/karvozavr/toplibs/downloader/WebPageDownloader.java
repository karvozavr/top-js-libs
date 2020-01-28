package ru.karvozavr.toplibs.downloader;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;

import java.util.Optional;

public interface WebPageDownloader {

    /**
     * Retrieves a page by the given URL.
     *
     * @param url URL to retrieve
     * @return the downloaded page
     */
    @NotNull
    Optional<Page> downloadPage(@NotNull String url);
}
