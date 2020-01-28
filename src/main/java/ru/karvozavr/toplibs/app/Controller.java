package ru.karvozavr.toplibs.app;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.downloader.SimpleWebPageDownloader;
import ru.karvozavr.toplibs.downloader.WebPageDownloader;
import ru.karvozavr.toplibs.search.DataExtractor;
import ru.karvozavr.toplibs.search.GoogleSearchEngine;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

    private final WebPageDownloader downloader;
    private final GoogleSearchEngine googleSearchEngine;
    private final DataExtractor dataExtractor;

    private static final Pattern JS_LIB_PATTERN = Pattern.compile("<script src=\"http.*/(.*?\\.js).*?\"");

    public Controller() {
        downloader = new SimpleWebPageDownloader();
        googleSearchEngine = new GoogleSearchEngine(downloader);
        dataExtractor = new DataExtractor(Arrays.asList(JS_LIB_PATTERN));
    }

    /**
     * Extracts top JS libs from top N search engine results for the given query.
     * @param query query to the search engine
     * @param topLibs top N libs
     * @param topResults amount of results
     * @return list of top N libs
     */
    @NotNull
    public List<String> topLibs(@NotNull String query, int topLibs, int topResults) {
        PriorityQueue<Map.Entry<String, Long>> top = new PriorityQueue<>(topLibs, (o1, o2) -> (int) (o1.getValue() - o2.getValue()));

        countLibsFromQueryResultPages(query, topResults).entrySet().forEach(entry -> {
            if (top.size() == topLibs) {
                if (entry.getValue() > top.peek().getValue()) {
                    top.poll();
                    top.offer(entry);
                }
            } else {
                top.offer(entry);
            }
        });

        ArrayList<String> libs = new ArrayList<>();
        while (!top.isEmpty()) {
            libs.add(top.poll().getKey());
        }
        Collections.reverse(libs);

        return libs;
    }

    @NotNull
    public Map<String, Long> countLibsFromQueryResultPages(@NotNull String query, int topResults) {
        return countLibs(googleSearchEngine.getTopNLinks(query, topResults).parallelStream(), Function.identity());
    }

    @NotNull
    private Map<String, Long> countLibs(@NotNull Stream<String> links, @NotNull Function<String, String> getLibName) {
        return links
            .map(downloader::downloadPage)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .flatMap(page -> dataExtractor.extractDataFromPage(page).stream())
            .collect(Collectors.groupingBy(getLibName, Collectors.counting()));
    }
}
