package ru.karvozavr.toplibs.app;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        App app = new App(controller);
        app.run();
    }
}
