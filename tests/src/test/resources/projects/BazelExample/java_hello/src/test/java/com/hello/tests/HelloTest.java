package com.hello.tests;


import static org.junit.Assert.*;

import org.hello.example.Hello;
import org.junit.Test;

public class HelloTest {

    Hello h = new Hello(){};

    @Test
    public void testPassing() {
        assertEquals(0, 0);
    }
}
