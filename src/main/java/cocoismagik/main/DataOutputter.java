package cocoismagik.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class DataOutputter {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;

    public static boolean writeToFile(Object object) {
        try {
            // Serialize the object to JSON
            String jsonEntry = gson.toJson(object);
    
            // Define the filename
            String filename = "output.json";
    
            // Get the path to the directory where the jar file is located
            URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarDir = Paths.get(url.toURI()).getParent();
            Path filePath = jarDir.resolve(filename).toAbsolutePath();
    
            // Check if the file exists
            if (!Files.exists(filePath)) {
                // If the file doesn't exist, create it
                Files.createFile(filePath);
                // Write the initial JSON array
                Files.write(filePath, Arrays.asList("["), StandardCharsets.UTF_8);
            }
    
            // Read the existing data from the file
            List<String> existingData = Files.readAllLines(filePath, StandardCharsets.UTF_8);
    
            // Check if the file is empty
            if (existingData.size() == 1 && existingData.get(0).equals("[")) {
                // If the file is empty, write the JSON object as the first element of the array
                existingData.set(0, "[" + jsonEntry + "]");
            } else {
                // If the file is not empty, remove the last closing bracket and append the new JSON object
                existingData.set(existingData.size() - 1, existingData.get(existingData.size() - 1).replaceAll("]$", ""));
                existingData.add(", " + jsonEntry + "]");
            }
    
            // Write the updated data back to the file
            Files.write(filePath, existingData, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
    
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to file: " + e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error writing to file: " + e.getMessage());
        }
        return false;
    }

    public static void logMessage(String message, int level) {
        level = Math.max(1, Math.min(3, level));
        String color = switch (level) {
            case INFO -> "\u001B[34m"; // Blue (Info)
            case WARNING -> "\u001B[33m"; // Yellow (Warning)
            case ERROR -> "\u001B[31m"; // Red (Error)
            default -> "";
        };
        String logLevel = switch (level) {
            case INFO -> "Info";
            case WARNING -> "Warning";
            case ERROR -> "Error";
            default -> "None";
        };
        String logTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String printMessage = color + "[" + logLevel + "]" + " - " + logTime + " - " + message + "\u001B[0m";
        String logMessage = "[" + logLevel + "]" + " - " + logTime + " - " + message + "\n";
        System.out.println(printMessage);
    
        try {
            Path jarPath = new File(System.getProperty("java.class.path")).toPath();
            Path logFile = jarPath.getParent().resolve(jarPath.getFileName().toString().replace(".jar", ".log"));
            Files.write(logFile, logMessage.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}

