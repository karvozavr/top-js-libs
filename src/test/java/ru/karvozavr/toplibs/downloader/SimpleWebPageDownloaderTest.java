package ru.karvozavr.toplibs.downloader;

import org.junit.Before;
import org.junit.Test;
import ru.karvozavr.toplibs.data.Page;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SimpleWebPageDownloaderTest {

    private WebPageDownloader downloader;

    @Before
    public void setup() {
        downloader = new SimpleWebPageDownloader();
    }

    @Test
    public void testDownloadOnePage() {
        checkPageHasText("http://duckduckgo.com", "duckduckgo");
    }

    @Test
    public void testDownloadOnePageHttps() {
        checkPageHasText("https://duckduckgo.com", "duckduckgo");
    }

    @Test
    public void testDownloadSeveralPages() {
        String[] urls = {"https://duckduckgo.com", "http://duckduckgo.com", "https://google.com"};
        for (String url : urls) {
            checkPageIsDownloaded(url);
        }
    }

    private void checkPageIsDownloaded(String url) {
        Page page = downloader.downloadPage(url).get();
        assertNotEquals("Result is not empty", "", page.getRawHtml());
    }

    private void checkPageHasText(String url, String hasText) {
        Page page = downloader.downloadPage(url).get();
        assertNotEquals("Result is not empty", "", page.getRawHtml());
        assertTrue("Page received some correct information", page.getRawHtml().contains(hasText));
    }
}