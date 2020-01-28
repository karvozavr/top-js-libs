package ru.karvozavr.toplibs.data;

import org.jetbrains.annotations.NotNull;

/**
 * Web page containing all the HTML data.
 */
public class Page {

    private String rawHtml;

    public Page(@NotNull String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public String getRawHtml() {
        return rawHtml;
    }
}
