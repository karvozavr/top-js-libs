package ru.karvozavr.toplibs.search;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;
import ru.karvozavr.toplibs.downloader.PageDownloadFailedError;
import ru.karvozavr.toplibs.downloader.WebPageDownloader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Search Engine implementation using Google Search.
 */
public class GoogleSearchEngine {

    public static final int RESULTS_PER_PAGE = 10;
    private static final String GOOGLE_SEARCH_PREFIX = "https://www.google.com/search?q=";
    private static final Pattern GOOGLE_RESULT_LINK_PATTERN =
        Pattern.compile("<div class=\"r\"><a href=\"(.*?)\"");

    private final WebPageDownloader downloader;
    private final DataExtractor dataExtractor;

    public GoogleSearchEngine(@NotNull WebPageDownloader downloader) {
        this.downloader = downloader;
        this.dataExtractor = new DataExtractor(Collections.singletonList(GOOGLE_RESULT_LINK_PATTERN));
    }

    /**
     * Retrieves top N urls of search results.
     *
     * @param query search query
     * @param topN  amount of results
     * @return top N results urls
     */
    @NotNull
    public List<String> getTopNLinks(@NotNull String query, int topN) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < topN; i += RESULTS_PER_PAGE) {
            Page googleResultsPage = downloader.downloadPage(encodeSearchQuery(query, i))
                .orElseThrow(() -> new PageDownloadFailedError("Failed to download google results."));
            List<String> links = dataExtractor.extractDataFromPage(googleResultsPage);
            final int resultsToFetch = Math.min(links.size(), topN - i);
            result.addAll(links.subList(0, resultsToFetch));
        }

        return result;
    }

    @NotNull
    private String encodeSearchQuery(@NotNull String query, int startFromResult) {
        try {
            return GOOGLE_SEARCH_PREFIX + URLEncoder.encode(query, StandardCharsets.UTF_8.name()) + "&start=" + startFromResult;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Java is broken, standard encoding UTF_8 unsupported.", e);
        }
    }
}
