package org.infraarmor.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileLoaderTest {
    @Test
    void loadFileContent_shouldReadFileContent(@TempDir Path tempDir) throws IOException {
        // Create a temporary file with content
        Path filePath = tempDir.resolve("testFile.txt");
        String fileContent = "Test file content";
        java.nio.file.Files.write(filePath, fileContent.getBytes());

        String result = FileLoader.loadFileContent(filePath.toString());

        assertEquals(fileContent, result);
    }

    @Test
    void loadFileContent_shouldThrowIOException_whenFileNotFound() {
        String nonExistentFilePath = "nonexistentfile.txt";
        assertThrows(IOException.class, () -> FileLoader.loadFileContent(nonExistentFilePath));
    }

    @Test
    void loadFileContent_shouldThrowIOException_whenIOExceptionOccurs(@TempDir Path tempDir) {
        Path directoryPath = tempDir.resolve("testDirectory");
        assertThrows(IOException.class, () -> FileLoader.loadFileContent(directoryPath.toString()));
    }
}
