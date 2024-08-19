package test;

import java.io.File;
import org.junit.platform.console.ConsoleLauncher;

public class Test {

    public static void main(String[] args) {
        // Check if the JAR path is provided
        if (args.length != 1) {
            System.err.println("Error: No JAR file path provided. Please provide the absolute path to the JAR file.");
            System.exit(1);
        }

        String jarPath = args[0];

        // Validate the provided JAR path
        File jarFile = new File(jarPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            System.err.println("Error: Provided path is invalid or does not point to a valid JAR file.");
            System.exit(1);
        }

        // Set the JAR path as a system property
        System.setProperty("jar.path", jarPath);

        // Run tests using the provided JAR path
        System.out.println("Running tests against JAR: " + jarPath);
        ConsoleLauncher.main(new String[]{
            "--scan-classpath"
        });
    }
}
