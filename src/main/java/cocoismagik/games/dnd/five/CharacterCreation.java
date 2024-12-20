package cocoismagik.games.dnd.five;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import cocoismagik.datastructures.ThreadManagementTracker;
import cocoismagik.datastructures.PlayerCharacters;
import cocoismagik.datastructures.TTRPGChar;
import cocoismagik.main.DataOutputter;
import cocoismagik.main.URLChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.ImageInfo;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.tika.Tika;

public class CharacterCreation {

    public static final String MENU_SEX_SELECT = "sex";
    public static final String MENU_ALIGNMENT_SELECT = "alignment";
    public static final String MENU_BACKGROUND_SELECT = "background";
    public static final String MENU_CLASS_SELECT = "class";
    public static final String MENU_RACE_SELECT = "race";
    public static final String MENU_ATTRIBUTE_METHOD_SELECT = "attribute_method";

    public static final String TEXT_INPUT_NAME = "dnd5e-name";
    public static final String TEXT_INPUT_URL = "dnd5e-url";
    public static final String TEXT_INPUT_DESCRIPTION = "dnd5e-desc";
    public static final String TEXT_INPUT_DETAIL_PERSONALITY = "dnd5e-detail-personality";
    public static final String TEXT_INPUT_DETAIL_IDEALS = "dnd5e-detail-ideals";
    public static final String TEXT_INPUT_DETAIL_BONDS = "dnd5e-detail-bonds";
    public static final String TEXT_INPUT_DETAIL_FLAWS = "dnd5e-detail-flaws";
    public static final String TEXT_INPUT_BACKSTORY = "dnd5e-backstory";

    public static final String MODAL_NAME = "dnd5e-modal-name";
    public static final String MODAL_URL = "dnd5e-modal-url";
    public static final String MODAL_DESCRIPTION = "dnd5e-modal-desc";
    public static final String MODAL_DETAIL = "dnd5e-detail-modal";
    public static final String MODAL_BACKSTORY = "dnd5e-modal-backstory";

    private static void sendCharacterDetailsEmbed(ThreadChannel thread) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Unnamed Character");
        embedBuilder.setDescription("As you make choices, this will get updated!");
        Message message = thread.sendMessageEmbeds(embedBuilder.build()).complete();
        ThreadManagementTracker.addThreadData(thread.getIdLong(), ThreadManagementTracker.DETAILS_EMBED, message.getIdLong());
    }

    private static void sendDetailsButtonsEmbed(ThreadChannel thread) {
        ActionRow buttonRow1 = ActionRow.of(
            Button.secondary("character-name-action", "Name"),
            Button.secondary("character-image-display", "Image URL"),
            Button.secondary("character-description", "Physical Description ")
        );

        ActionRow buttonRow2 = ActionRow.of(
            Button.secondary("character-backstory", "Write Character Backstory"),
            Button.secondary("character-details", "Details and Traits"),
            Button.primary("character-randomize", "Randomize")
        );

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Set Character Details");
        embedBuilder.setDescription("Click each button for a prompt to enter your character's details. "
        +"\n\nFor image URLs, some external resources might work, but if it's getting rejected you can upload a picture in this chat and "
        +"copy that link instead. To do that you will need to download the image and then upload it from your computer, not just paste the "
        +"link in the chat."
        +"\n\nTo randomize your character details, just hit the randomize button and it will generate a random details for you, but not an "
        +"image, you'll need to provide that link yourself still.");

        MessageCreateAction action = thread.sendMessageEmbeds(embedBuilder.build());
        action = action.setComponents(buttonRow1, buttonRow2);
        Message message = action.complete();
        
        ThreadManagementTracker.addThreadData(thread.getIdLong(), ThreadManagementTracker.BUTTONS_EMBED, message.getIdLong());
    }

    private static void sendSexAlignmentBackgroundEmbed(ThreadChannel thread) {
        ActionRow sexSelectMenu = ActionRow.of(
            StringSelectMenu.create("sex-selection")
                .setPlaceholder("Select character sex")
                .addOption("Male", "male")
                .addOption("Female", "female")
                .build()
        );

        ActionRow alignmentSelectMenu = ActionRow.of(
            StringSelectMenu.create("alignment-selection")
                .setPlaceholder("Select character alignment")
                .addOption("Lawful Good", "lawful-good")
                .addOption("Neutral Good", "neutral-good")
                .addOption("Chaotic Good", "chaotic-good")
                .addOption("Lawful Neutral", "lawful-neutral")
                .addOption("Neutral", "neutral")
                .addOption("Chaotic Neutral", "chaotic-neutral")
                .addOption("Lawful Evil", "lawful-evil")
                .addOption("Neutral Evil", "neutral-evil")
                .addOption("Chaotic Evil", "chaotic-evil")
                .build()
        );

        ActionRow backgroundSelectMenu = ActionRow.of(
            StringSelectMenu.create("background-selection")
                .setPlaceholder("Select character background")
                .addOption("Acolyte", "acolyte")
                .addOption("Charlatan", "charlatan")
                .addOption("Criminal", "criminal")
                .addOption("Entertainer", "entertainer")
                .addOption("Folk Hero", "folk-hero")
                .addOption("Guild Artisan", "guild-artisan")
                .addOption("Hermit", "hermit")
                .addOption("Noble", "noble")
                .addOption("Outlander", "outlander")
                .addOption("Sage", "sage")
                .addOption("Sailor", "sailor")
                .addOption("Soldier", "soldier")
                .addOption("Urchin", "urchin")
                .build()
        );

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Select Sex, Alignment, and Background");
        embedBuilder.setDescription("You will be prompted for further details after you make selections.");

        MessageCreateAction action = thread.sendMessageEmbeds(embedBuilder.build());
        action = action.setComponents(sexSelectMenu, alignmentSelectMenu, backgroundSelectMenu);
        action.queue();
    }

    private static void sendClassSelectionEmbed(ThreadChannel thread) {
        ActionRow raceSelectMenu = ActionRow.of(
            StringSelectMenu.create("race-selection")
                .setPlaceholder("Select character race")
                .addOption("Dragonborn", "dragonborn")
                .addOption("Dwarf (Hill)", "dwarf-hill")
                .addOption("Dwarf (Mountain)", "dwarf-mountain")
                .addOption("Elf (Drow)", "elf-drow")
                .addOption("Elf (High)", "elf-high")
                .addOption("Elf (Wood)", "elf-wood")
                .addOption("Gnome (Forest)", "gnome-forest")
                .addOption("Gnome (Rock)", "gnome-rock")
                .addOption("Half-Elf", "half-elf")
                .addOption("Half-Orc", "half-orc")
                .addOption("Halfling (Lightfoot)", "halfling-lightfoot")
                .addOption("Halfling (Stout)", "halfling-stout")
                .addOption("Human (Base)", "human-base")
                .addOption("Human (Variant)", "human-variant")
                .addOption("Tiefling", "tiefling")
                .build()
        );

        ActionRow classSelectMenu = ActionRow.of(
            StringSelectMenu.create("class-selection")
                .setPlaceholder("Select character class")
                .addOption("Barbarian", "barbarian")
                .addOption("Bard", "bard")
                .addOption("Cleric", "cleric")
                .addOption("Druid", "druid")
                .addOption("Fighter", "fighter")
                .addOption("Monk", "monk")
                .addOption("Paladin", "paladin")
                .addOption("Ranger", "ranger")
                .addOption("Rogue", "rogue")
                .addOption("Sorcerer", "sorcerer")
                .addOption("Warlock", "warlock")
                .addOption("Wizard", "wizard")
                .build()
        );

        ActionRow attributeSelectMenu = ActionRow.of(
            StringSelectMenu.create("attribute-selection")
                .setPlaceholder("Select attribute method")
                .addOption("Point Buy", "point-buy")
                .addOption("Standard Array", "standard-array")
                .addOption("4d6 Drop Lowest", "4d6-drop-lowest")
                .build()
        );

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Select Race, Class, and Attribute Method");
        embedBuilder.setDescription("You will be prompted for further details after you make selections.");

        MessageCreateAction action = thread.sendMessageEmbeds(embedBuilder.build());
        action = action.setComponents(raceSelectMenu, classSelectMenu, attributeSelectMenu);
        action.queue();
    }

    public static void initialThreadSetup(StringSelectInteractionEvent event){
        // Need to ensure that the character exists and is associated with the player for later embeds to function
        PlayerCharacters pcs = PlayerCharacters.getInstance();
        Long playerID = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        pcs.addCharacter(playerID, new TTRPGChar(playerID, threadID));

        // Wait for complete to make sure the character exists before allowing interactions with it
        event.getHook().sendMessage("Selected: " + event.getValues().get(0)).complete();

        // Send the embeds
        sendCharacterDetailsEmbed(event.getChannel().asThreadChannel());
        sendDetailsButtonsEmbed(event.getChannel().asThreadChannel());
        sendSexAlignmentBackgroundEmbed(event.getChannel().asThreadChannel());
        sendClassSelectionEmbed(event.getChannel().asThreadChannel());
    }

    private static void makeButtonGreen(Message message, TTRPGChar character){
        ActionRow buttonRow1;
        ActionRow buttonRow2;

        // Name to check name button
        boolean hasName = character.getDetail("name") != null;
        
        // Check image button
        boolean hasImage = character.getDetail("url") != null;

        // Check description button
        boolean hasDescription = character.getDetail("desc") != null;
        
        // Check details and traits button
        boolean hasDetailsAndTraits = character.getDetail("detail-personality") != null
            && character.getDetail("detail-ideals") != null
            && character.getDetail("detail-bonds") != null
            && character.getDetail("detail-flaws") != null;
        
        // Check backstory button
        boolean hasBackstory = character.getDetail("backstory") != null;

        buttonRow1 = ActionRow.of(
            ((hasName) ? Button.success("character-name-action", "Name") : Button.secondary("character-name-action", "Name")),
            ((hasImage) ? Button.success("character-image-display", "Image URL") : Button.secondary("character-image-display", "Image URL")),
            ((hasDescription) ? Button.success("character-description", "Physical Description") : Button.secondary("character-description", "Physical Description"))
        );

        buttonRow2 = ActionRow.of(
            ((hasBackstory) ? Button.success("character-backstory", "Write Character Backstory") : Button.secondary("character-backstory", "Write Character Backstory")),
            ((hasDetailsAndTraits) ? Button.success("character-details", "Details and Traits") : Button.secondary("character-details", "Details and Traits")),
            Button.primary("character-randomize", "Randomize")
        );

        EmbedBuilder characterDetailsEmbed = new EmbedBuilder();
        characterDetailsEmbed.setTitle("Set Character Details");
        characterDetailsEmbed.setDescription("Click each button for a prompt to enter your character's details. "
        +"\n\nFor image URLs, some external resources might work, but if it's getting rejected you can upload a picture in this chat and "
        +"copy that link instead. To do that you will need to download the image and then upload it from your computer, not just paste the "
        +"link in the chat."
        +"\n\nTo randomize your character details, just hit the randomize button and it will generate a random details for you, but not an "
        +"image, you'll need to provide that link yourself still.");

        MessageEditAction messageEditAction = message.editMessageEmbeds(characterDetailsEmbed.build());
        messageEditAction.setComponents(buttonRow1, buttonRow2);
        messageEditAction.queue();
    }

    private static Message missingDetailsEmbedRecover(ThreadChannel channel) {
        // Create a new Details Embed to replace the old one and track it properly
        EmbedBuilder characterDetailsEmbed = new EmbedBuilder();
        characterDetailsEmbed.setTitle("Character Detail Options");
        characterDetailsEmbed.setDescription("Set your character details.");

        MessageCreateAction messageAction = channel.sendMessageEmbeds(characterDetailsEmbed.build());
        Message message = messageAction.complete();

        return message;
    }

    /**
     * Adds a detail to a character if it exists, otherwise creates a new character
     * and adds the detail.
     * 
     * @param detailName the name of the detail to add
     * @param detailValue the value of the detail to add
     * @param playerID the ID of the player who owns the character
     * @param channelID the ID of the channel where the character was created
     * @return the character with the added detail
     */
    private static TTRPGChar addDetailsToCharacter(String detailName, String detailValue, Long playerID, Long channelID) {
        DataOutputter.logMessage("Adding detail to character in thread "+channelID, DataOutputter.INFO);
        detailName = detailName.toLowerCase();
        detailValue = detailValue.toLowerCase();
        PlayerCharacters pcs = PlayerCharacters.getInstance();
        List<TTRPGChar> characters = pcs.getCharacters(playerID); // Get the characters for the player

        DataOutputter.logMessage("Player has " + characters.size() + " characters:\n" + characters.toString(), DataOutputter.INFO);

        for (TTRPGChar character : characters) {
            if (character.getOriginThreadId().equals(channelID)) {
                character.addDetail(detailName, detailValue);
                return character;
            }
        }

        DataOutputter.logMessage("Character not found, creating new character", DataOutputter.WARNING);
        // If the character was not found, create a new character and add the detail
        TTRPGChar newCharacter = new TTRPGChar(playerID, channelID);
        newCharacter.addDetail(detailName, detailValue);
        pcs.addCharacter(playerID, newCharacter);
        return newCharacter;
    }

    /**
     * Returns the file extension corresponding to the given MIME content type.
     *
     * This method supports common image content types and standardizes certain
     * extensions (e.g., ".jpeg" and ".JPG" are standardized to ".jpg").
     *
     * @param contentType the MIME type of the content (e.g., "image/jpeg")
     * @return the file extension associated with the content type (e.g., ".jpg"),
     *         or null if the content type is unsupported or unknown
     */
    private static String getFileExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
    
        switch (contentType.toLowerCase()) { // Case-insensitive check
            case "image/jpeg":
                return ".jpg"; // Standardize to .jpg for .jpeg/.JPG/.JPEG
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp"; 
            default:
                return null; // Unsupported or unknown type
        }
    }

    /**
     * Downloads an image from the given URL using the curl command.
     * 
     * The method will throw an exception if the curl command fails to execute
     * successfully or if the downloaded file is invalid or empty.
     * 
     * The method will also detect the file type of the downloaded file and
     * append the appropriate file extension to the file name.
     * 
     * The method will also delete the downloaded file on exit.
     * 
     * @param url the URL of the image to download
     * @return the downloaded file
     * @throws Exception if the curl command fails to execute successfully or
     *                   if the downloaded file is invalid or empty
     */
    public static File downloadImageWithCurl(String url) throws Exception {
        // Create a temporary directory for the download
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).toAbsolutePath();

        String hashedFileName = UUID.nameUUIDFromBytes(url.getBytes()).toString();
        File tempFile = new File(tempDir.toFile(), hashedFileName);

        // Build the curl command as a list of arguments
        List<String> command = new ArrayList<>();
        command.add("curl");
        command.add("--http3");
        command.add("-L");
        command.add("-A");
        command.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        command.add("--raw");
        command.add("--output");
        command.add(tempFile.getAbsolutePath());
        command.add(url);

        // Execute the command using ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // Merge stdout and stderr for debugging
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        // Check if the command executed successfully
        if (exitCode != 0) {
            String errorOutput = new String(process.getInputStream().readAllBytes());
            throw new RuntimeException("Curl command failed with exit code " + exitCode + ": " + errorOutput);
        }

        // Validate the downloaded file
        if (!tempFile.exists() || tempFile.length() == 0) {
            throw new RuntimeException("Downloaded file is invalid or empty: " + tempFile.getAbsolutePath());
        }

        // Append appropriate file type from data
        String mimeType = (new Tika()).detect(tempFile);
        String fileExtension = getFileExtensionFromContentType(mimeType);

        if (fileExtension == null) {
            throw new RuntimeException("Unsupported file type: " + (mimeType != null ? mimeType : "null"));
        } else {
            // Append file extension
            File tempFileWithExtension = new File(tempFile.getParent(), hashedFileName + fileExtension);
            if (!tempFile.renameTo(tempFileWithExtension)) {
                throw new RuntimeException("Failed to rename downloaded file: " + tempFile.getAbsolutePath());
            }
            tempFile = tempFileWithExtension;
        }

        // Cleanup
        tempFile.deleteOnExit();
        //TODO: delete files after awhile so we can use a cache system

        return tempFile;
    }

    /**
     * Downloads an image from the given URL and saves it to a temporary file.
     * The file is deleted when the program exits.
     * 
     * @param imageUrl The URL of the image to download.
     * @return The temporary file containing the image, or null if the download failed.
     */
    private static File downloadImage(String imageUrl) {
        File tempFile;
        try {
            tempFile = downloadImageWithCurl(imageUrl);
        } catch (Exception e) {
            DataOutputter.logMessage("Failed to download image: " + e.getMessage(), DataOutputter.ERROR);
            return null;
        }
    
        DataOutputter.logMessage("Image downloaded to " + tempFile.getAbsolutePath(), DataOutputter.INFO);
        return tempFile;
    }

    /**
     * Updates the details embed for a character to reflect the current state of the character
     * 
     * @param character The character to update the details embed for
     * @param message   The message that contains the details embed to update
     */
    private static void updateDetailsEmbed(TTRPGChar character, Message message){
        DataOutputter.logMessage("Updating character details embed", DataOutputter.INFO);
        
        // Get all the details from the character.
        Object nameObj = character.getDetail("name");
        String name = (nameObj == null) ? "" : nameObj.toString();
        Object sexObj = character.getDetail("sex");
        String sex = (sexObj == null) ? "" : sexObj.toString();
        Object aligObj = character.getDetail("alignment");
        String alig = (aligObj == null) ? "" : aligObj.toString();
        Object imagObj = character.getDetail("url");
        String imag = (imagObj == null) ? "" : imagObj.toString();
        Object descObj = character.getDetail("desc");
        String desc = (descObj == null) ? "" : descObj.toString();
        Object personalityObj = character.getDetail("detail-personality");
        String personality = (personalityObj == null) ? "" : personalityObj.toString();
        Object idealsObj = character.getDetail("detail-ideals");
        String ideals = (idealsObj == null) ? "" : idealsObj.toString();
        Object bondsObj = character.getDetail("detail-bonds");
        String bonds = (bondsObj == null) ? "" : bondsObj.toString();
        Object flawsObj = character.getDetail("detail-flaws");
        String flaws = (flawsObj == null) ? "" : flawsObj.toString();
        Object backstoryObj = character.getDetail("backstory");
        String backstory = (backstoryObj == null) ? "" : backstoryObj.toString();
        Object raceObj = character.getDetail("race");
        String race = (raceObj == null) ? "" : raceObj.toString();
        Object classObj = character.getDetail("class");
        String gameClass = (classObj == null) ? "" : classObj.toString();
        Object levelObj = character.getDetail("level");
        String level = (levelObj == null) ? "1" : levelObj.toString();
        Object backgroundObj = character.getDetail("background");
        String background = (backgroundObj == null) ? "" : backgroundObj.toString();

        String basicInfoString = "Level " + level + " " + gameClass + ", " + alig + ", " + sex + ", " + race + ", " + background;

        MessageEmbed oldEmbed = message.getEmbeds().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        // Set the description to the character information
        embedBuilder.addField("Basic Details", basicInfoString, true);
        embedBuilder.addField("Traits", personality, true);
        embedBuilder.addField("Flaws", flaws, true);
        embedBuilder.addField("Ideals", ideals, true);
        embedBuilder.addField("Bonds", bonds, true);
        embedBuilder.addField("Physical Description", desc, true);
        embedBuilder.addField("backstory", backstory, false);

        // Set the title to the character's name
        embedBuilder.setTitle(((name != "") ? name : "Unnamed Character")+"'s Details");

        ThreadChannel threadChannel = message.getChannel().asThreadChannel();
        if (threadChannel != null) {
            if (imag == null || imag.equals("")) {
                message.editMessageEmbeds(embedBuilder.build()).queue();
            } else {
                File tempImage;
                try {
                    tempImage = downloadImage(imag);
                    if (tempImage == null) {
                        throw new IllegalArgumentException("Failed to download image");
                    }
                } catch (IllegalArgumentException e) {
                    DataOutputter.logMessage("Error while downloading: "+e.getMessage(), DataOutputter.WARNING);
                    ImageInfo oldImage = oldEmbed.getImage();
                    if (oldImage == null) {
                        embedBuilder.setImage(null);
                    } else {
                        embedBuilder.setImage(oldImage.getUrl());
                    }
                    message.editMessageAttachments(message.getAttachments()).setEmbeds(embedBuilder.build()).queue();
                    return;
                }
                embedBuilder.setImage("attachment://" + tempImage.getName());
                message.editMessageAttachments(FileUpload.fromData(tempImage)).setEmbeds(embedBuilder.build()).queue();
            }
        }
    }

    /**
     * Handles a StringSelectInteractionEvent with a custom ID that corresponds to a specific character detail.
     * 
     * The method sends a confirmation message to the user, adds the selected detail to the character, and then updates the details embed of the character.
     * 
     * @param event The StringSelectInteractionEvent.
     * @param detailName The name of the detail that was selected.
     */
    public static void handleAnyMenu(StringSelectInteractionEvent event, String detailName){
        event.getHook().sendMessage("You choose: " + event.getValues().get(0) + "!").setEphemeral(true).queue();

        Long playerID = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();

        TTRPGChar character = addDetailsToCharacter(detailName, event.getValues().get(0), playerID, threadID);

        Long detailsEmbedMessageID = ThreadManagementTracker.getThreadData(event.getChannelIdLong(), ThreadManagementTracker.DETAILS_EMBED);
        Message detailsEmbedMessage;
        if (detailsEmbedMessageID == null) {
            String s = "Couldn't find the embedMessageID for " + event.getChannelIdLong();
            DataOutputter.logMessage(s, DataOutputter.WARNING);
            detailsEmbedMessage = missingDetailsEmbedRecover(event.getChannel().asThreadChannel()); //FIXME: check if we are in a thread
        } else {
            detailsEmbedMessage = event.getChannel().asThreadChannel().retrieveMessageById(detailsEmbedMessageID).complete();
        }
        updateDetailsEmbed(character, detailsEmbedMessage);
    }

    /**
     * Processes the input from a modal interaction event and updates the character details accordingly.
     *
     * This method retrieves the input value from the modal, validates it, and associates it with the
     * corresponding character attribute. It then updates the character's details in the embedded message
     * within the thread channel.
     * 
     * @param event The modal interaction event containing the user's input.
     * @param value The identifier for the modal input value to be processed.
     */
    public static void handleAnyModalInput(ModalInteractionEvent event, String[] values) {
        for (String value : values) {
            ModalMapping nameMapping = event.getValue(value);

            String prefix = "dnd5e-";
            String normValue = value.substring(prefix.length());
            
            if (nameMapping == null) {
                event.getHook().sendMessage("Your input is invalid").queue();
                DataOutputter.logMessage("Null input for " + value + " modal", DataOutputter.WARNING);
                continue;
            }

            if (nameMapping.getAsString().equals(TEXT_INPUT_URL)) {
                String url = nameMapping.getAsString();
                try {
                    boolean isSafeUrl = URLChecker.isSafeUrl(url, new String[]{"jpg", "jpeg", "png", "gif", "webp"});
                    if (!isSafeUrl) {
                        throw new IllegalArgumentException("Unsafe URL detected");
                    }
                } catch (IllegalArgumentException e) {
                    event.getHook().sendMessage("URL rejected because: "+e.getMessage()).queue();
                    DataOutputter.logMessage("Error parsing URL: "+e.getMessage(), DataOutputter.ERROR);
                    continue;
                } catch (Exception e) {
                    event.getHook().sendMessage("Something went wrong!").queue();
                    DataOutputter.logMessage("Unexpected error handling URL input for DND character creation flow: "+ e.getMessage(), DataOutputter.WARNING);
                    continue;
                }
            }
    
            event.getHook().sendMessage("You submitted "+nameMapping.getAsString()+" for "+normValue).setEphemeral(true).complete();
    
            Long playerID = event.getUser().getIdLong();
            Long threadID = event.getChannel().getIdLong();
    
            TTRPGChar character = addDetailsToCharacter(normValue, nameMapping.getAsString(), playerID, threadID);
            
            Long detailsEmbedMessageID = ThreadManagementTracker.getThreadData(event.getChannelIdLong(), ThreadManagementTracker.DETAILS_EMBED);
            Message detailsEmbedMessage;
            if (detailsEmbedMessageID == null) {
                String s = "Couldn't find the embedMessageID for " + event.getChannelIdLong();
                DataOutputter.logMessage(s, DataOutputter.WARNING);
                detailsEmbedMessage = missingDetailsEmbedRecover(event.getChannel().asThreadChannel()); //FIXME: check if we are in a thread
            } else {
                detailsEmbedMessage = event.getChannel().asThreadChannel().retrieveMessageById(detailsEmbedMessageID).complete();
            }
            updateDetailsEmbed(character, detailsEmbedMessage);
    
            Long buttonsEmbedMessageID = ThreadManagementTracker.getThreadData(event.getChannelIdLong(), ThreadManagementTracker.BUTTONS_EMBED);
            Message buttonsEmbedMessage;
            if (buttonsEmbedMessageID == null) {
                String s = "Couldn't find the embedMessageID for " + event.getChannelIdLong();
                DataOutputter.logMessage(s, DataOutputter.WARNING);
                //FIXME: create recover method
                continue;
            } else {
                buttonsEmbedMessage = event.getChannel().asThreadChannel().retrieveMessageById(buttonsEmbedMessageID).complete();
            }
            makeButtonGreen(buttonsEmbedMessage, character);
        }
    }

    public static void handleAnyModalButton(ButtonInteractionEvent e, String mID, String[] ids, String[] l, TextInputStyle[] s, String[] p){
        // List of text inputs to add
        ArrayList<TextInput> inputs = new ArrayList<TextInput>(); 

        for (int i = 0; i < ids.length; i++){
            inputs.add(
                TextInput.create(ids[i], l[i], s[i])
                    .setRequired(true)
                    .setMinLength(1)
                    .setPlaceholder(p[i])
                    .build()
            );
        }

        // Ternary to check for the case where there is multiple inputs
        Modal.Builder modalBuilder = Modal.create(mID, (l.length > 1) ? "Character Details" : l[0]);
        for (TextInput input : inputs){
            modalBuilder.addComponents(ActionRow.of(input));
        }
        Modal modal = modalBuilder.build();

        // Send the modal
        try{
            e.replyModal(modal).complete();
        }catch(Exception ee){
            e.getHook().sendMessage("There was an error processing the request.").setEphemeral(true).queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
            return;
        }
    }

    public static void handleCharacterRandomization(ButtonInteractionEvent event){
        event.getHook().sendMessage("Not implemented yet").queue();

        //TODO: implement randomization
        // Choose a random name
        // Choose a random physical description
        // Choose a random backstory
        // Choose random personality traits and flaws
        // Choose random ideals and bonds
    }
}
