package cocoismagik.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class DataOutputter {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static boolean writeToFile(Object object) {
        try {
            // Serialize the object to JSON
            String jsonEntry = gson.toJson(object);

            // Define the filename and path
            String filename = "output.json";
            Path filePath = Path.of(filename).toAbsolutePath();

            // Ensure the file exists
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Write JSON to the file, appending it
            Files.write(filePath, Collections.singletonList(jsonEntry), StandardCharsets.UTF_8, StandardOpenOption.APPEND);

            // Output the JSON to the console for verification
            System.out.println(jsonEntry);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }
}

