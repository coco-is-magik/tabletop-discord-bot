package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class DataOutputterTest {
    /**
     * Tests the writeToFile method in the DataOutputter class within a JAR file.
     * 
     * The test first checks if the JAR path is provided and if the file exists.
     * It then creates a URLClassLoader to load the DataOutputter class and invokes the writeToFile method.
     * The test checks if the method returns true and if the output JSON file is valid.
     * 
     * @return  does not return any value, it only checks if the method invocation is successful
     */
    @Test
    public void testWriteToFile_CompletesWithNoErrors() {
        // Testing that the JAR runs without throwing any exceptions
        assertDoesNotThrow(() -> {
            /*
             * Test if the JAR path is provided and if the file exists
             */
            String jarPath = System.getProperty("jar.path");
            if (jarPath == null || jarPath.isEmpty()) {
                throw new IllegalArgumentException("Error: JAR path not provided. Please provide the absolute path to the JAR file.");
            }

            File jarFile = new File(jarPath);
            if (!jarFile.exists() || !jarFile.isFile()) {
                throw new IllegalArgumentException("Error: Provided path is invalid or does not point to a valid JAR file.");
            }

            /*
             * Create a URLClassLoader to load the DataOutputter class
             */
            URL jarUrl = null; // URL for jar
            try{
                jarUrl = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/"); // Specify jar: and file:
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Error: Malformed URL", e);
            }

            // Load the DataOutputter class
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
            // Check we havent somehow gotten this far with a null clazz
            if (clazz == null) {
                classLoader.close(); // Avoid resource leak in this case
                throw new AssertionError("Class not found in JAR file");
            }
            
            // Test objects
            List<Object> testObjects = new ArrayList<>();
            testObjects.add("test");
            testObjects.add(null);

            // Call the writeToFile method
            try {
                // Get the writeToFile method
                Method method = clazz.getMethod("writeToFile", Object.class);
                // Invoke the writeToFile method
                // Iterate over the test objects
                for (Object testObject : testObjects) {
                    // Invoke the writeToFile method
                    Object returnValue;
                    returnValue = method.invoke(null, testObject);
                    
                    // Check if the writeToFile method returned the expected value
                    if (!(Boolean.class.isInstance(returnValue))) {
                        boolean boolReturned = Boolean.class.cast(returnValue);
                        if(!boolReturned) {
                            throw new AssertionError("writeToFile method returned false instead of true for test object: " + testObject);
                        }
                    }
                }

                // Test that the JSON file is actually a proper json file
                // Create a new Gson instance for JSON parsing
                Gson gson = new Gson();

                // Get the URL of the JAR file
                URL myUrl = new URL("file:" + jarFile.getAbsolutePath());

                // Convert the URL to a URI and escape any spaces in the path
                URI uri = new URI(myUrl.toString().replace(" ", "%20")); // escape spaces

                // Get the parent directory of the JAR file
                Path jarDir = Paths.get(uri).getParent();

                // Resolve the path to the output JSON file
                Path filePath = jarDir.resolve("output.json").toAbsolutePath();

                // Create a Reader to read the contents of the JSON file
                Reader reader = new FileReader(filePath.toFile());

                // Use Gson to parse the JSON file into a Java object
                gson.fromJson(reader, Object.class);

                // TODO: verify contents of JSON file
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


    /**
     * Tests the logMessage method with complex inputs and no errors.
     *
     * This test case loads the DataOutputter class from a JAR file, calls the logMessage method with different test logs,
     * and verifies that the method returns true and the log messages are written to the log file.
     *
     * @return void
     */
    @Test
    public void testLogMessage_ComplesesWithNoErrors() {
        assertDoesNotThrow(() -> {
            /*
             * Test if the JAR path is provided and if the file exists
             */
            String jarPath = System.getProperty("jar.path");
            if (jarPath == null || jarPath.isEmpty()) {
                throw new IllegalArgumentException("Error: JAR path not provided. Please provide the absolute path to the JAR file.");
            }

            File jarFile = new File(jarPath);
            if (!jarFile.exists() || !jarFile.isFile()) {
                throw new IllegalArgumentException("Error: Provided path is invalid or does not point to a valid JAR file.");
            }

            /*
             * Create a URLClassLoader to load the DataOutputter class
             */
            URL jarUrl = null; // URL for jar
            try{
                jarUrl = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/"); // Specify jar: and file:
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Error: Malformed URL", e);
            }

            // Load the DataOutputter class
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
            // Check we havent somehow gotten this far with a null clazz
            if (clazz == null) {
                classLoader.close(); // Avoid resource leak in this case
                throw new AssertionError("Class not found in JAR file");
            }
            
            // Test log message
            HashMap<String, Integer> pairs = new HashMap<>();
            pairs.put("test info", 1);
            pairs.put("test warning", 2);
            pairs.put("test error", 3);

            // Call the logMessage method
            try {
                // Get the logMessage method
                Method method = clazz.getMethod("logMessage", String.class, int.class);
                // Iterate over the test logs
                for (Map.Entry<String, Integer> entry : pairs.entrySet()) {
                    String message = entry.getKey();
                    int level = entry.getValue();
                    Object returnValue = method.invoke(null, message, level);
                    if (!(Boolean.class.isInstance(returnValue))) {
                        boolean boolReturned = Boolean.class.cast(returnValue);
                        if(!boolReturned) {
                            classLoader.close();
                            throw new AssertionError("logMessage method returned false instead of true for test object: " + message);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                // This exception occurs if the specified method is not found in the class
                classLoader.close();
                throw new AssertionError("Method not found in class: " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                // This exception occurs if the method is not accessible (e.g., if it's private)
                classLoader.close();
                throw new AssertionError("Method is not accessible: " + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                // This exception occurs if the invoked method throws an exception
                classLoader.close();
                throw new AssertionError("Method invocation failed: " + e.getTargetException().getMessage(), e);
            } catch (IllegalArgumentException e) {
                // This exception occurs if the method is invoked with incorrect arguments
                throw new AssertionError("Method invocation failed with incorrect arguments: " + e.getMessage(), e);
            } catch (FileSystemNotFoundException e) {
                // This exception occurs if the file system is not found
                throw new AssertionError("File system not found: " + e.getMessage(), e);
            }

            Path jarFilePath = new File(jarFile.getAbsolutePath()).toPath();
            Path logFilePath = jarFilePath.getParent().resolve("tabletop-discord-bot.log");
            File logFile = logFilePath.toFile();
            if (!logFile.exists()) {
                classLoader.close();
                throw new AssertionError("Log file not found: " + logFilePath);
            }
            if (!logFile.isFile()) {
                classLoader.close();
                throw new AssertionError("Log file path is not a file: " + logFilePath);
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Iterator<Map.Entry<String, Integer>> iterator = pairs.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Integer> entry = iterator.next();
                        String expectedMessage = entry.getKey();
                        if (line.contains(expectedMessage)) {
                            // Log message found, remove it from the map
                            iterator.remove();
                        }
                    }
                }
                if (!pairs.isEmpty()) {
                    throw new AssertionError("Not all log messages found in log file: " + pairs.keySet());
                }
            } catch (IOException e) {
                classLoader.close();
                throw new AssertionError("Error reading log file: " + e.getMessage(), e);
            }

            classLoader.close();
        });
    }
}
