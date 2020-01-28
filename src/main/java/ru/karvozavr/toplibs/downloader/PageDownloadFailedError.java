package ru.karvozavr.toplibs.downloader;

/**
 * Exception thrown in the case of page download failure by any reason.
 */
public class PageDownloadFailedError extends RuntimeException {

    public PageDownloadFailedError(String message) {
        super(message);
    }
}
