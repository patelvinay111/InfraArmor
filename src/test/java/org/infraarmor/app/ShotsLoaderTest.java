package org.infraarmor.app;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ShotsLoaderTest {

    @Test
    void loadShotsContent_shouldLoadShotsContent(@TempDir Path tempDir) throws IOException {
        // Create a temporary directory with test shots
        Path shotsDir = tempDir.resolve("shots");
        java.nio.file.Files.createDirectory(shotsDir);

        Path shot1 = shotsDir.resolve("shot1.txt");
        Path shot2 = shotsDir.resolve("shot2.txt");
        java.nio.file.Files.write(shot1, "Shot 1 content".getBytes());
        java.nio.file.Files.write(shot2, "Shot 2 content".getBytes());

        String result = ShotsLoader.loadShotsContent(shotsDir.toString());

        assertTrue(result.contains("Shot 1 content"));
        assertTrue(result.contains("Shot 2 content"));
    }

    @Test
    void loadShotsContent_shouldReturnEmptyString_whenNoShotsFound(@TempDir Path tempDir) throws IOException {
        // Create a temporary directory without any shots
        String result = ShotsLoader.loadShotsContent(tempDir.toString());

        assertEquals("", result);
    }

    @Test
    void loadShotsContent_shouldLoadShotsContent_fromExistingDirectory(@TempDir Path tempDir) throws IOException {
        // Create a temporary directory with test shots
        Path shotsDir = tempDir.resolve("shots");
        java.nio.file.Files.createDirectory(shotsDir);

        Path shot1 = shotsDir.resolve("shot1.txt");
        java.nio.file.Files.write(shot1, "Shot 1 content".getBytes());

        String result = ShotsLoader.loadShotsContent(shotsDir.toString());

        assertTrue(result.contains("Shot 1 content"));
    }
}
