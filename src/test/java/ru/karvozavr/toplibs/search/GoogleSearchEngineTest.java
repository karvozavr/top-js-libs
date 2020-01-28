package ru.karvozavr.toplibs.search;

import org.junit.Before;
import org.junit.Test;
import ru.karvozavr.toplibs.downloader.SimpleWebPageDownloader;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GoogleSearchEngineTest {

    private GoogleSearchEngine searchEngine;

    @Before
    public void setup() {
        searchEngine = new GoogleSearchEngine(new SimpleWebPageDownloader());
    }

    @Test
    public void getTopNLinksSmoke() {
        final List<String> results = searchEngine.getTopNLinks("Ada Lovelace", 3);
        assertEquals("3 results retrieved", 3, results.size());
        assertTrue("Result is a link", results.get(0).startsWith("http"));
    }

    @Test
    public void testSeveralResultPages() {
        final List<String> results = searchEngine.getTopNLinks("RFC 4960", 23);
        assertEquals("23 results retrieved", 23, results.size());
        for (String result : results) {
            assertTrue("Result is a link", result.startsWith("http"));
        }
    }

    @Test
    public void testEmptyQuery() {
        final List<String> results = searchEngine.getTopNLinks("", 23);
        assertTrue("No results", results.isEmpty());
    }
}