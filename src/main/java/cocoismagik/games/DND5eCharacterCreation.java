package cocoismagik.games;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import cocoismagik.datastructures.CharacterCreationThreadDetailsEmbedTracker;
import cocoismagik.datastructures.PlayerCharacters;
import cocoismagik.datastructures.TTRPGChar;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.ImageInfo;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class DND5eCharacterCreation {
    /**
     * Handles the button interaction for the button with the custom ID
     * "character-name-action". This method creates a modal with a text input for
     * the user to enter a name for their character. The method then sends this
     * modal to the user.
     * 
     * This method will throw an error if the event has already been acknowledged.
     * 
     * @param event The event that triggered this method.
     */
    public static void handleCharacterNameAction(ButtonInteractionEvent event){
        TextInput nameInput = TextInput.create("dnd5e-name", "Character Name", TextInputStyle.SHORT)
            .setRequired(true)
            .setRequiredRange(1, 100)
            .setPlaceholder("Name of the Character")
            .build();
        
        Modal modal = Modal.create("dnd5e-name-modal", "Character Name")
            .addComponents(ActionRow.of(nameInput))
            .build();

        try{
            event.replyModal(modal).queue();
        }catch(Exception e){
            event.getHook().sendMessage("There was an error processing the request.").queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
        }
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-url". This method creates a modal with a text input for the user
     * to enter a URL for their character's image. The method then sends this
     * modal to the user.
     * 
     * This method will throw an error if the event has already been acknowledged.
     *
     * @param event The event that triggered this method.
     */
    public static void handleCharacterImageDisplay(ButtonInteractionEvent event){
        TextInput urlInput = TextInput.create("dnd5e-url", "Character Image", TextInputStyle.SHORT)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("URL for character image")
            .build();
        
        Modal modal = Modal.create("dnd5e-image-modal", "Character Image")
            .addComponents(ActionRow.of(urlInput))
            .build();
        
        try{
            event.replyModal(modal).queue();
        }catch(Exception e){
            event.getHook().sendMessage("There was an error processing the request.").queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
        }
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-desc". This method creates a modal with a text input for the user
     * to enter their character's physical description. The method then sends
     * this modal to the user.
     * 
     * This method will throw an error if the event has already been acknowledged.
     * 
     * @param event The event that triggered this method.
     */
    public static void handleCharacterDescription(ButtonInteractionEvent event){
        TextInput urlInput = TextInput.create("dnd5e-desc", "Character Description", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Physical description of character")
            .build();
        
        Modal modal = Modal.create("dnd5e-desc-modal", "Character Description")
            .addComponents(ActionRow.of(urlInput))
            .build();

        try{
            event.replyModal(modal).queue();
        }catch(Exception e){
            event.getHook().sendMessage("There was an error processing the request.").queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
        }
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-details". This method creates a modal with text inputs for the user
     * to enter their character's personality traits, ideals, bonds, and flaws.
     * The method then sends this modal to the user.
     * 
     * This method will throw an error if the event has already been acknowledged.
     * 
     * @param event The event that triggered this method.
     */
    public static void handleCharacterDetails(ButtonInteractionEvent event){
        TextInput personalityInput = TextInput.create("dnd5e-detail-personality", "Character Personality Traits", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Personality traits")
            .build();
        
        TextInput idealsInput = TextInput.create("dnd5e-detail-ideals", "Character Ideals", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Character ideals")
            .build();
        
        TextInput bondsInput = TextInput.create("dnd5e-detail-bonds", "Character Bonds", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Character bonds")
            .build();
        
        TextInput flawsInput = TextInput.create("dnd5e-detail-flaws", "Character Personality Flaws", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Personality flaws")
            .build();
        
        Modal modal = Modal.create("dnd5e-detail-modal", "Character Description")
            .addComponents(ActionRow.of(personalityInput), ActionRow.of(idealsInput), ActionRow.of(bondsInput), ActionRow.of(flawsInput))
            .build();
        
        try{
            event.replyModal(modal).queue();
        }catch(Exception e){
            event.getHook().sendMessage("There was an error processing the request.").queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
        }
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-backstory". The method creates a modal with a text input for the
     * user to enter their character's backstory. The method then sends this
     * modal to the user.
     * 
     * This method will throw an error if the event has already been acknowledged.
     * 
     * @param event The event that triggered this method.
     */
    public static void handleCharacterBackstory(ButtonInteractionEvent event){
        TextInput backstoryInput = TextInput.create("dnd5e-backstory", "Character Backstory", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Character backstory")
            .build();
        
        Modal modal = Modal.create("dnd5e-backstory-modal", "Character Description")
            .addComponents(ActionRow.of(backstoryInput))
            .build();

        try{
            event.replyModal(modal).queue();
        }catch(Exception e){
            event.getHook().sendMessage("There was an error processing the request.").queue();
            DataOutputter.logMessage("Attempted to send modal but the event was already acknowledged!", DataOutputter.ERROR);
        }
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

    public static File downloadImageWithCurl(String url) throws Exception {
        // Create a temporary directory for the download
        Path tempDir = Files.createTempDirectory("curl_download_temp");
        File tempFile = new File(tempDir.toFile(), "downloaded_image.jpg"); //FIXME: support other file types and name this properly

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

        return tempFile;
    }

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
     * Updates the embed sent by the bot in the channel with the character's details.
     * 
     * @param character The character to update the embed with.
     * @param message The message containing the embed to update.
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
        Object attrObject = character.getDetail("attribute_method");
        String attrMethod = (attrObject == null) ? "" : attrObject.toString();

        String logBlock = "Character details in embed:\n" +
            "Name: " + name + "\n" +
            "Sex: " + sex + "\n" +
            "Alignment: " + alig + "\n" +
            "Image: " + imag + "\n" +
            "Description: " + desc + "\n" +
            "Personality traits: " + personality + "\n" +
            "Ideals: " + ideals + "\n" +
            "Bonds: " + bonds + "\n" +
            "Flaws: " + flaws + "\n" +
            "Backstory: " + backstory + "\n" +
            "Race: " + race + "\n" +
            "Class: " + gameClass + "\n" +
            "Level: " + level + "\n" +
            "Background: " + background + "\n" +
            "Attribute Method: " + attrMethod + "\n";
        DataOutputter.logMessage(logBlock, DataOutputter.INFO);

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
        embedBuilder.addField("backstory", background, false);

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

    private static void handleAnyMenu(StringSelectInteractionEvent event, String detailName){
        event.getHook().sendMessage("You choose: " + event.getValues().get(0) + "!").queue();

        Long playerID = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();

        TTRPGChar character = addDetailsToCharacter(detailName, event.getValues().get(0), playerID, threadID);

        Long detailsEmbedMessageID = CharacterCreationThreadDetailsEmbedTracker.getDetailsEmbedMessageLong(event.getChannelIdLong());
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

    public static void handleSexSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "sex");
    }

    public static void handleAlignmentSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "alignment");
    }

    public static void handleRaceSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "race");
    }

    public static void handleClassSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "class");
    }

    public static void handleBackgroundSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "background");
    }

    public static void handleAttributeMethodSelectionMenu(StringSelectInteractionEvent event) {
        handleAnyMenu(event, "attribute_method");
    }

    private static void handleAnyModalInput(ModalInteractionEvent event, String value) {
        //FIXME: image does not update right
        ModalMapping nameMapping = event.getValue(value);

        String prefix = "dnd5e-";
        String normValue = value.substring(prefix.length());
        

        if (nameMapping == null) {
            event.getHook().sendMessage("Your input is invalid").queue();
            DataOutputter.logMessage("Null input for " + value + " modal", DataOutputter.WARNING);
            return;
        }

        event.getHook().sendMessage("You submitted "+nameMapping.getAsString()+" for "+normValue).complete();

        Long playerID = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();

        TTRPGChar character = addDetailsToCharacter(normValue, nameMapping.getAsString(), playerID, threadID);
        
        Long detailsEmbedMessageID = CharacterCreationThreadDetailsEmbedTracker.getDetailsEmbedMessageLong(event.getChannelIdLong());
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

    public static void handleCharacterName(ModalInteractionEvent event) {
        handleAnyModalInput(event, "dnd5e-name");
    }

    public static void handleCharacterImage(ModalInteractionEvent event) {
        handleAnyModalInput(event, "dnd5e-url");
    }

    public static void handleCharacterDescription(ModalInteractionEvent event) {
        handleAnyModalInput(event, "dnd5e-desc");
    }

    public static void handleCharacterDetail(ModalInteractionEvent event) {
        handleAnyModalInput(event, "dnd5e-detail-personality");
        handleAnyModalInput(event, "dnd5e-detail-ideals");
        handleAnyModalInput(event, "dnd5e-detail-bonds");
        handleAnyModalInput(event, "dnd5e-detail-flaws");
    }

    public static void handleCharacterBackstory(ModalInteractionEvent event) {
        handleAnyModalInput(event, "dnd5e-backstory");
    }


}
