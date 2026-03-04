package com.demotour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void mainWithValidAccountRunsWithoutError() {
        assertDoesNotThrow(() -> Main.main(new String[]{"ACC-001"}));
    }
}

