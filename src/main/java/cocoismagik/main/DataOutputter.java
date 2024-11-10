package cocoismagik.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
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
    public static final int TERM = 4;

    /**
     * Writes the given object to a file in JSON format.
     *
     * @param  object  the object to be written to the file
     * @return          true if the object was successfully written to the file, false otherwise
     * @throws IllegalArgumentException if the object is null
     * @throws IOException if an I/O error occurs while writing to the file
     * @throws URISyntaxException if the URI of the file is not valid
     */
    public static boolean writeToFile(Object object) throws IllegalArgumentException, IOException, URISyntaxException {
        if (object == null) {
            logMessage("Attempted to write null value to file", WARNING);
            return true;
        }
        try {
            // Serialize the object to JSON
            String jsonEntry = gson.toJson(object);
    
            // Define the filename
            String filename = "output.json";
    
            // Get the path to the directory where the jar file is located
            URL url = DataOutputter.class.getProtectionDomain().getCodeSource().getLocation();
            URI uri = new URI(url.toString().replace(" ", "%20")); // escape spaces
            Path jarDir = Paths.get(uri).getParent();
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

    /**
     * Logs a message to the console and writes it to a log file.
     * 
     * The message is logged with a specific level (INFO, WARNING, or ERROR) and a timestamp.
     * The log level is used to determine the color of the message in the console.
     * 
     * @param message the message to be logged
     * @param level   the level of the message (1 = INFO, 2 = WARNING, 3 = ERROR)
     * @return true if the message was successfully written to the log file, false otherwise
     */
    public static boolean logMessage(String message, int level) {
        System.out.print("\033[2K"); // Clear the current line
        System.out.print("\033[G"); // Move cursor to the beginning of the line
        System.out.flush();
        String userPrompt = "(tabletop discord bot) >> ";
        level = Math.max(1, Math.min(4, level));
        String color = switch (level) {
            case INFO -> "\u001B[34m"; // Blue (Info)
            case TERM -> "\u001B[32m"; // Green (End)
            case WARNING -> "\u001B[33m"; // Yellow (Warning)
            case ERROR -> "\u001B[31m"; // Red (Error)
            default -> "";
        };
        String logLevel = switch (level) {
            case INFO -> "Info";
            case TERM -> "End";
            case WARNING -> "Warning";
            case ERROR -> "Error";
            default -> "None";
        };
        String logTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String printMessage = color + "[" + logLevel + "]" + " - " + logTime + " - " + message + "\u001B[0m";
        String logMessage = "[" + logLevel + "]" + " - " + logTime + " - " + message + "\n";
        System.out.println(printMessage);
        if(level != TERM) {
            System.out.print(userPrompt);
        }
        
        try {
            // Get the path to the directory where the jar file is located
            String filename = "tabletop-discord-bot.log";
            URL url = DataOutputter.class.getProtectionDomain().getCodeSource().getLocation();
            URI uri = new URI(url.toString().replace(" ", "%20")); // escape spaces
            Path jarDir = Paths.get(uri).getParent();
            Path filePath = jarDir.resolve(filename).toAbsolutePath();
            //Path jarPath = new File(System.getProperty("java.class.path")).toPath();
            //Path logFilePath = jarPath.getParent().resolve(jarPath.getFileName().toString().replace(".jar", ".log"));
            Files.write(filePath, logMessage.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            return false;
        } catch (URISyntaxException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            return false;
        }
        return true;
    }
}

