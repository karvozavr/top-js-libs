package ru.karvozavr.toplibs.search;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.data.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * May be used to extract arbitrary data from the <code>Page</code>
 * using a set of regex matchers.
 */
public class DataExtractor {

    private final List<Pattern> patterns;

    public DataExtractor(@NotNull List<Pattern> patterns) {
        this.patterns = patterns;
    }

    @NotNull
    public List<String> extractDataFromPage(@NotNull Page page) {
        ArrayList<String> result = new ArrayList<>();
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(page.getRawHtml());

            while (matcher.find()) {
                result.add(matcher.group(1));
            }
        }

        return result;
    }
}
