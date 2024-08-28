package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.jupiter.api.Test;

public class EntryTest {

    private static String jarPath = null;
    private static File jarFile = null;
    private static URL jarUrl = null;
    private static URLClassLoader classLoader = null;
    private static Class<?> clazz = null;

    /**
     * Performs standard checks for the JAR file, including verifying the JAR path, 
     * checking if the file exists, and loading the Entry class using a URLClassLoader.
     * 
     * @throws IllegalArgumentException if the JAR path is not provided, the file does not exist, 
     *         or the path is invalid
     * @throws AssertionError if the class is not found in the JAR file, a security manager 
     *         denies creation of the class loader, or the class definition is not found
     */
    private static void standardChecks() throws IllegalArgumentException, AssertionError{

        /*
         * Test if the JAR path is provided and if the file exists
         */
        String jarPath = System.getProperty("jar.path");
        if (jarPath == null || jarPath.isEmpty()) {
            throw new IllegalArgumentException("Error: JAR path not provided. Please provide the absolute path to the JAR file.");
        }
        EntryTest.jarPath = jarPath;

        File jarFile = new File(jarPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            throw new IllegalArgumentException("Error: Provided path is invalid or does not point to a valid JAR file.");
        }
        EntryTest.jarFile = jarFile;

        /*
         * Create a URLClassLoader to load the Entry class
         */
        URL jarUrl = null; // URL for jar
        try{
            jarUrl = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/"); // Specify jar: and file:
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Error: Malformed URL", e);
        }
        EntryTest.jarUrl = jarUrl;

        // Load the Entry class
        URLClassLoader classLoader = null;
        Class<?> clazz = null;
        try {
            // Create a URLClassLoader to load classes from the JAR file
            classLoader = new URLClassLoader(new URL[] { jarUrl });
            // Try to load a class from the JAR file
            clazz = classLoader.loadClass("cocoismagik.rulebook.Entry");
        } catch (ClassNotFoundException e) {
            // This exception occurs if the specified class is not found in the JAR file
            throw new AssertionError("Class not found in JAR file: " + e.getMessage(), e);
        } catch (SecurityException e) {
            // This exception occurs if a security manager exists and its checkCreateClassLoader method denies creation of the class loader
            throw new AssertionError("Security manager denied creation of URLClassLoader", e);
        } catch (NoClassDefFoundError e) {
            // This error occurs if the class definition is not found (e.g., if the JAR file is corrupted)
            throw new AssertionError("Class definition not found: " + e.getMessage(), e);
        }
        // Check we havent somehow gotten this far with a null clazz
        if (clazz == null) {
            throw new AssertionError("Class not found in JAR file");
        }

        EntryTest.classLoader = classLoader;
        EntryTest.clazz = clazz;
    }

    /**
     * Reverts the state of the EntryTest class to its initial state after performing standard checks.
     * 
     * This method closes the URLClassLoader, and sets all static fields (jarPath, jarFile, jarUrl, classLoader, clazz) to null.
     * 
     * @throws IOException if an I/O error occurs while closing the class loader
     */
    private static void undoStandardChecks() throws IOException {
        classLoader.close();
        EntryTest.jarPath = null;
        EntryTest.jarFile = null;
        EntryTest.jarUrl = null;
        EntryTest.classLoader = null;
        EntryTest.clazz = null;
    }

    /**
     * Tests the constructor of the loaded class to ensure it completes with no errors.
     *
     * This test case loads the class using the URLClassLoader, gets the constructor with the matching parameter types,
     * instantiates the object, and asserts that the returned values are as expected.
     *
     * @return does not return any value, it only checks if the constructor invocation is successful
     */
    @Test
    public void constructor_CompletesWithNoErrors(){
        assertDoesNotThrow(() ->{
            try{
                standardChecks();
            } catch (IllegalArgumentException | AssertionError e) {
                throw new AssertionError("Error in constructor: " + e.getMessage(), e);
            }

            // Test Area
            // Parameters
            String title = "Test Title";
            String category = "Test Category";
            String markdown = "Test Markdown";

            // Load the class using the URLClassLoader
            Class<?> loadedClass = classLoader.loadClass(clazz.getName());
            
            // Get the constructor with the matching parameter types
            Constructor<?> constructor = loadedClass.getConstructor(title.getClass(), category.getClass(), markdown.getClass());
            
            // Instantiate the object using the loaded class and constructor
            Object object = constructor.newInstance(title, category, markdown);

            Method methodGetTitle = clazz.getMethod("getTitle");
            Method methodGetCategory = clazz.getMethod("getCategory");
            Method methodGetMarkdown = clazz.getMethod("getMarkdownText");

            Object returnedTitle = methodGetTitle.invoke(object);
            Object returnedCategory = methodGetCategory.invoke(object);
            Object returnedMarkdown = methodGetMarkdown.invoke(object);

            // Assert
            // Not null
            assert returnedTitle != null;
            assert returnedCategory != null;
            assert returnedMarkdown != null;

            // Type String
            assert returnedTitle instanceof String;
            assert returnedCategory instanceof String;
            assert returnedMarkdown instanceof String;

            // Value is as expected
            assertEquals(title, returnedTitle);
            assertEquals(category, returnedCategory);
            assertEquals(markdown, returnedMarkdown);

            // Clean
            try {
                undoStandardChecks();
            } catch (IOException e) {
                throw new AssertionError("Error in undoStandardChecks: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Tests if the setters and getters of a class complete with no errors.
     * 
     * This test case checks if the class can be loaded, instantiated, and its 
     * setters and getters can be invoked without throwing any exceptions.
     * 
     * @return void
     */
    @Test
    public void settersAndGetters_CompletesWithNoErrors(){
        assertDoesNotThrow(() ->{
            try{
                standardChecks();
            } catch (IllegalArgumentException | AssertionError e) {
                throw new AssertionError("Error in constructor: " + e.getMessage(), e);
            }
            // Test Area
            // Parameters
            String title = "Test Title";
            String category = "Test Category";
            String markdown = "Test Markdown";

            // Load the class using the URLClassLoader
            Class<?> loadedClass = classLoader.loadClass(clazz.getName());
            
            // Get the constructor with the matching parameter types
            Constructor<?> constructor = loadedClass.getConstructor(title.getClass(), category.getClass(), markdown.getClass());
            
            // Instantiate the object using the loaded class and constructor
            Object object = constructor.newInstance(title, category, markdown);

            // Get the methods
            Method methodGetTitle = clazz.getMethod("getTitle");
            Method methodGetCategory = clazz.getMethod("getCategory");
            Method methodGetMarkdown = clazz.getMethod("getMarkdownText");
            Method methodSetTitle = clazz.getMethod("setTitle", String.class);
            Method methodSetCategory = clazz.getMethod("setCategory", String.class);
            Method methodSetMarkdown = clazz.getMethod("setMarkdownText", String.class);

            // Setters
            methodSetTitle.invoke(object, "New Title");
            methodSetCategory.invoke(object, "New Category");
            methodSetMarkdown.invoke(object, "New Markdown");

            // Getters
            Object returnedTitle = methodGetTitle.invoke(object);
            Object returnedCategory = methodGetCategory.invoke(object);
            Object returnedMarkdown = methodGetMarkdown.invoke(object);

            // Assert
            // Not null
            assert returnedTitle != null;
            assert returnedCategory != null;
            assert returnedMarkdown != null;

            // Type String
            assert returnedTitle instanceof String;
            assert returnedCategory instanceof String;
            assert returnedMarkdown instanceof String;

            // Value is as expected
            assertEquals("New Title", returnedTitle);
            assertEquals("New Category", returnedCategory);
            assertEquals("New Markdown", returnedMarkdown);

            // Clean
            try {
                undoStandardChecks();
            } catch (IOException e) {
                throw new AssertionError("Error in undoStandardChecks: " + e.getMessage(), e);
            }
        });
    }
}
