package ru.karvozavr.toplibs.app;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ControllerTest {

    Controller controller;

    @Before
    public void setup() {
        controller = new Controller();
    }

    @Test
    public void countLibsFromQueryResultPagesSmoke() {
        Map<String, Long> top = controller.countLibsFromQueryResultPages("Ada Lovelace", 15);
        assertFalse(top.isEmpty());
    }

    @Test
    public void topLibsSmoke() {
        List<String> top = controller.topLibs("Ada Lovelace", 5, 15);
        assertEquals(5, top.size());
    }
}