package test;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;



public class DataOutputterTest {
    @Test
    public void testWriteToFile_CompletesWithNoErrors() {
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

            // Create a URL for the JAR file
            URL jarUrl = null;
            try{
                jarUrl = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/");
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Error: Malformed URL", e);
            }

            URLClassLoader classLoader = null;
            Class<?> clazz = null;
            try {
                // Create a URLClassLoader to load classes from the JAR file
                classLoader = new URLClassLoader(new URL[] { jarUrl });
                // Try to load a class from the JAR file
                clazz = classLoader.loadClass("cocoismagik.main.DataOutputter");
            } catch (ClassNotFoundException e) {
                // This exception occurs if the specified class is not found in the JAR file
                classLoader.close(); // Avoid resource leak in this case
                throw new AssertionError("Class not found in JAR file: " + e.getMessage(), e);
            } catch (SecurityException e) {
                // This exception occurs if a security manager exists and its checkCreateClassLoader method denies creation of the class loader
                throw new AssertionError("Security manager denied creation of URLClassLoader", e);
            } catch (NoClassDefFoundError e) {
                // This error occurs if the class definition is not found (e.g., if the JAR file is corrupted)
                throw new AssertionError("Class definition not found: " + e.getMessage(), e);
            }
            if (clazz == null) {
                classLoader.close(); // Avoid resource leak in this case
                throw new AssertionError("Class not found in JAR file");
            }
            
            // Test objects
            String testString = "test";

            // Call the writeToFile method
            try {
                // Get the writeToFile method
                Method method = clazz.getMethod("writeToFile", Object.class);
                // Invoke the writeToFile method
                Object returnValue = method.invoke(null, testString);
                // Check if the writeToFile method returned the expected value
                if (!(Boolean.class.isInstance(returnValue))) {
                    boolean boolReturned = Boolean.class.cast(returnValue);
                    if(!boolReturned) {
                        throw new AssertionError("writeToFile method returned false instead of true");
                    }
                }

                Gson gson = new Gson();
                URL myUrl = new URL("file:" + jarFile.getAbsolutePath());
                URI uri = new URI(myUrl.toString().replace(" ", "%20")); // escape spaces
                Path jarDir = Paths.get(uri).getParent();
                Path filePath = jarDir.resolve("output.json").toAbsolutePath();
                Reader reader = new FileReader(filePath.toFile());
                gson.fromJson(reader, Object.class);
            } catch (NoSuchMethodException e) {
                // This exception occurs if the specified method is not found in the class
                throw new AssertionError("Method not found in class: " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                // This exception occurs if the method is not accessible (e.g., if it's private)
                throw new AssertionError("Method is not accessible: " + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                // This exception occurs if the invoked method throws an exception
                throw new AssertionError("Method invocation failed: " + e.getTargetException().getMessage(), e);
            } catch (IllegalArgumentException e) {
                // This exception occurs if the method is invoked with incorrect arguments
                throw new AssertionError("Method invocation failed with incorrect arguments: " + e.getMessage(), e);
            } catch (IOException e) {
                // This exception occurs if an I/O error occurs
                throw new AssertionError("I/O error occurred: " + e.getMessage(), e);
            } catch (JsonSyntaxException e) {
                // This exception occurs if the JSON is invalid
                throw new AssertionError("Invalid JSON: " + e.getMessage(), e);
            } catch (FileSystemNotFoundException e) {
                // This exception occurs if the file system is not found
                throw new AssertionError("File system not found: " + e.getMessage(), e);
            } finally {
                // Close the class loader
                classLoader.close();
            }
        });
    }
}
