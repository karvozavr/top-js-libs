package ru.karvozavr.toplibs.app;

import org.jetbrains.annotations.NotNull;
import ru.karvozavr.toplibs.downloader.PageDownloadFailedError;

import java.util.List;
import java.util.Scanner;

public class App {

    public static final int TOP_LIBS_NUMBER = 5;
    private static final int TOP_RESULTS_NUMBER = 15;
    private final Controller controller;

    public App(@NotNull Controller controller) {
        this.controller = controller;
    }

    public void run() {
        String query = readQuery();
        try {
            List<String> topLibs = controller.topLibs(query, TOP_LIBS_NUMBER, TOP_RESULTS_NUMBER);
            printTopLibs(topLibs);
        } catch (PageDownloadFailedError e) {
            System.err.println("Failed to retrieve google search results.");
            e.printStackTrace(System.err);
        } catch (RuntimeException e) {
            System.err.println("Unexpected error occurred.");
            e.printStackTrace(System.err);
        }
    }

    @NotNull
    private String readQuery() {
        System.out.println("Enter the query:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void printTopLibs(List<String> topLibs) {
        System.out.println("Top " + TOP_LIBS_NUMBER + " JS libs among first " + TOP_RESULTS_NUMBER + " Google results:");
        int number = 1;
        for (String lib : topLibs ) {
            System.out.println(number + ". " + lib);
            ++number;
        }
    }
}
