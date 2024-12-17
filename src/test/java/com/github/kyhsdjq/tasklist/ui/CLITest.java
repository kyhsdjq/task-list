package com.github.kyhsdjq.tasklist.ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    @Test
    void askForStringSucceed() {
        String simulatedInput = "Hello, World!\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        assertEquals("Hello, World!", CLI.askForString("", null));
    }

    @Test
    void askForStringFail() {
        String simulatedInput = "Hello, World!\ny\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        assertEquals("y", CLI.askForString("", List.of("y", "n")));
    }

    @Test
    void askForIntSucceed() {
        String simulatedInput = "54321\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        assertEquals(54321, CLI.askForInt(""));
    }

    @Test
    void askForIntFail() {
        String simulatedInput = "not an int\n54321\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        assertEquals(54321, CLI.askForInt(""));
    }

    @Test
    void askForLocalDateTimeSucceed() {
        String simulatedInput = "2024\n12\n16\n17\n45\n";  // 模拟的多次输入
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        LocalDateTime result = CLI.askForLocalDateTime();
        assertEquals(LocalDateTime.of(2024, 12, 16, 17, 45), result);
    }

    @Test
    void askForLocalDate() {
        String simulatedInput = "2024\n12\n16\n";  // 模拟的多次输入
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        LocalDate result = CLI.askForLocalDate();
        assertEquals(LocalDate.of(2024, 12, 16), result);
    }

    @Test
    void askForDuration() {
        String simulatedInput = "16\n17\n45\n";  // 模拟的多次输入
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(byteArrayInputStream);
        Duration result = CLI.askForDuration();
        assertEquals(Duration.ofDays(16).plusHours(17).plusMinutes(45), result);
    }
}