package cocoismagik.games;

import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

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
        
        Modal modal = Modal.create("dnd5e-desc-modal", "Character Description")
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

    public static void handleSexSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement sex selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }

    public static void handleAlignmentSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement alignment selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }

    public static void handleRaceSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement race selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }

    public static void handleClassSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement class selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }

    public static void handleBackgroundSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement background selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }

    public static void handleAttributeMethodSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement attribute selection for dnd5e
        event.getHook().sendMessage("Not implemented yet").queue();
    }
}
