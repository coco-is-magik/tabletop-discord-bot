package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.util.concurrent.TimeUnit;

class MainTest {

    @Test
    void testMainRunsWithoutExceptions() {
        // Testing that the JAR runs without throwing any exceptions
        assertDoesNotThrow(() -> {
            String jarPath = System.getProperty("jar.path");
            if (jarPath == null || jarPath.isEmpty()) {
                throw new IllegalArgumentException("Error: JAR path not provided. Please provide the absolute path to the JAR file.");
            }

            File jarFile = new File(jarPath);
            if (!jarFile.exists() || !jarFile.isFile()) {
                throw new IllegalArgumentException("Error: Provided path is invalid or does not point to a valid JAR file.");
            }

            // Run the JAR file
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath);
            processBuilder.redirectErrorStream(true); // Merge stdout and stderr

            try {
                Process process = processBuilder.start();
                boolean completed = process.waitFor(30, TimeUnit.SECONDS);
                if (!completed) {
                    throw new RuntimeException("Error: The process did not complete in time.");
                }

                int exitCode = process.exitValue();
                if (exitCode != 0) {
                    throw new RuntimeException("Error: The JAR execution failed with exit code " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error: Exception occurred while running the JAR file.", e);
            }
        });
    }
}
