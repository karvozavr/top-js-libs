package ru.karvozavr.toplibs.downloader;

/**
 * Exception thrown in the case of page download failure by any reason.
 */
public class PageDownloadFailedError extends RuntimeException {

    public PageDownloadFailedError() {
        super();
    }

    public PageDownloadFailedError(String message) {
        super(message);
    }

    public PageDownloadFailedError(String message, Throwable cause) {
        super(message, cause);
    }

    public PageDownloadFailedError(Throwable cause) {
        super(cause);
    }
}
