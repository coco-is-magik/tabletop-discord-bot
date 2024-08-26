package cocoismagik.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class ApiDataManager {
    // Final static variable to hold the single instance of the class
    private static final ApiDataManager INSTANCE = new ApiDataManager();
    private static String tokenString = null;

    // Private constructor to prevent instantiation
    private ApiDataManager() {
        DataOutputter.logMessage("ApiDataManager initializing", DataOutputter.INFO);
        try {
            /*
             * Get the config file
             */
            //Filename = "config/tabletop-discord-bot.config";
            String filename = "config/tabletop-discord-bot.config";

            // Get the path to the directory where the jar file is located
            URL url = DataOutputter.class.getProtectionDomain().getCodeSource().getLocation();

            // Remove any spaces in the path for URI to play nice
            URI uri = new URI(url.toString().replace(" ", "%20"));

            // Get the jar directory
            Path jarDir = Paths.get(uri).getParent();

            // Find the config file
            Path filePath = jarDir.resolve(filename).toAbsolutePath();

            // Read the config file into a string
            String jString = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

            /*
             * Extract info from the config file
             */
            // Create a Gson instance
            Gson gson = new Gson();

            // Parse the JSON into a JsonObject
            JsonObject jsonObject = gson.fromJson(jString, JsonObject.class);

            // Extract fields
            tokenString = jsonObject.get("token").getAsString();
        } catch (URISyntaxException e) {
            DataOutputter.logMessage("Error getting config file: " + e.getMessage(), DataOutputter.ERROR);
        } catch (IOException e) {
            DataOutputter.logMessage("Error getting config file: " + e.getMessage(), DataOutputter.ERROR);
        } finally {
            if (tokenString == null) {
                DataOutputter.logMessage("Token not found in config file", DataOutputter.WARNING);
                // TODO: abort logging into Discord
            } else {
                DataOutputter.logMessage("Found token in config file", DataOutputter.INFO);
                // Log into Discord and pull bot info
                JDABuilder.createDefault(tokenString)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("Tabletop Games"))
                .build();
            }
        }
        
    }

    // Public method to provide access to the single instance
    public static ApiDataManager getInstance() {
        return INSTANCE;
    }
}
