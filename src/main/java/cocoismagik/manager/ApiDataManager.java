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

import cocoismagik.commands.Ping;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ApiDataManager {
    // Final static variable to hold the single instance of the class
    private static ApiDataManager instance = null;
    private static String tokenString = null;
    private static Long testServerId = null;
    private static boolean testGuildFound = false;
    private static boolean testingCommandsAdded = false;
    private static boolean globalCommandsAdded = false;
    private static JDA jda = null;
    private static Guild testingGuild = null;

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
            testServerId = jsonObject.get("test_server_id").getAsLong();
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
                try {
                    ApiDataManager.jda = JDABuilder.createDefault(tokenString)
                        .setStatus(OnlineStatus.ONLINE)
                        .setActivity(Activity.playing("Tabletop Games"))
                        .build()
                        .awaitReady();
                } catch (InterruptedException e) {
                    DataOutputter.logMessage("Error logging into Discord: " + e.getMessage(), DataOutputter.ERROR);
                }
                if (ApiDataManager.jda == null) {
                    DataOutputter.logMessage("JDA not initialized", DataOutputter.ERROR);
                }
            }
        }
        DataOutputter.logMessage("ApiDataManager initialized", DataOutputter.INFO);
    }

    // Public method to provide access to the single instance
    public static ApiDataManager getInstance() {
        // We must wait to make the new instance until this is called because otherwise awaitready wont halt the thread
        if (instance == null) {
            instance = new ApiDataManager();
        }
        return instance;
    }

    public static void logAllGuilds() {
        if (ApiDataManager.jda != null) {
            DataOutputter.logMessage("Logging all guilds", DataOutputter.INFO);
            for (Guild guild : ApiDataManager.jda.getGuilds()) {
                DataOutputter.logMessage("Connected to guild: " + guild.getName() + " (" + guild.getId() + ")", DataOutputter.INFO);
            }
        } else {
            DataOutputter.logMessage("Attempted to log all guilds before JDA initialized", DataOutputter.ERROR);
        }
    }

    public static void addEventListeners() {
        if (ApiDataManager.jda != null) {
            ApiDataManager.jda.addEventListener(new Ping());
        } else {
            DataOutputter.logMessage("Attempted to add event listeners before JDA initialized", DataOutputter.ERROR);
        }
    }

    public static void getTestingGuild() {
        if (ApiDataManager.jda != null) {
            if (testServerId != null) {
                testingGuild = ApiDataManager.jda.getGuildById(testServerId); // FIXME: take this as a command line argument or something
                if (testingGuild != null) {
                    testGuildFound = true;
                } else {
                    DataOutputter.logMessage("Testing guild not found", DataOutputter.ERROR);
                }
            } else {
                DataOutputter.logMessage("Test server ID not found in config file", DataOutputter.ERROR);
            }
        } else {
            DataOutputter.logMessage("Attempted to find testing guild before JDA initialized", DataOutputter.ERROR);
        }
    }

    public static void addTestingCommands() {
        if (ApiDataManager.jda != null) {
            if (!testingCommandsAdded) {
                if (testGuildFound) {
                    testingGuild.updateCommands().addCommands(
                        Commands.slash("ping", "Ping the bot")
                            .addOption(OptionType.STRING, "message", "The message to send", false)
                    ).queue();
                    testingCommandsAdded = true;
                } else {
                    DataOutputter.logMessage("Attempted to add testing commands before testing guild found", DataOutputter.ERROR);
                }
            }
        } else {
            DataOutputter.logMessage("Attempted to add testing commands before JDA initialized", DataOutputter.ERROR);
        }
    }
}
