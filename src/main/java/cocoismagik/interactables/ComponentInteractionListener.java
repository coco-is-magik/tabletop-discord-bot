package cocoismagik.interactables;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import cocoismagik.datastructures.PlayerCharacters;
import cocoismagik.datastructures.TTRPGChar;
import cocoismagik.datastructures.ThreadOwnershipTracker;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class ComponentInteractionListener extends ListenerAdapter {

    /**
     * Checks if the user is allowed to interact with the components of the given
     * event. The user is allowed to interact with the components if they own the
     * thread that the interaction event is happening in.
     * 
     * If the user is not allowed to interact with the components, the method will
     * send an ephemeral message to the user saying that they cannot interact with
     * the components because it is not their thread.
     * 
     * The method will also log a message at the INFO level if the user is not
     * allowed to interact with the components.
     * 
     * @param event The event containing the components the user is trying to
     *              interact with.
     * @return True if the user is allowed to interact with the components, false
     *         otherwise.
     */
    private boolean checkInteractionValidity(GenericComponentInteractionCreateEvent event){
        event.deferReply().setEphemeral(true).queue();

        long userId = event.getUser().getIdLong();

        if (!ThreadOwnershipTracker.ownsThread(userId, event.getChannelIdLong())) {
            String s = "User named "+event.getUser().getName()+" with id "+event.getUser().getIdLong()
                        +" attempted to interact with thread "+event.getChannelIdLong()+" but does not own it.";
            DataOutputter.logMessage(s, DataOutputter.INFO);
            event.getHook().sendMessage("You cannot interact with these components beccause this is not your thread.").queue();
            return false;
        }

        GuildMessageChannel channel = event.getChannel().asGuildMessageChannel();

        // Check if the channel is a private thread
        if (!(channel instanceof ThreadChannel)) {
            // Log or handle the case where it's not a private thread
            String s = "Character creation flow outside of thread context. User was "
                    +event.getUser().getName()+" with id "+event.getUser().getIdLong()
                    +" and the channel was "+channel.getName()+" with id "+channel.getIdLong();
            DataOutputter.logMessage(s, DataOutputter.WARNING);
        }
        return true;
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "character-name-action". This method creates a modal with a text input for
     * the user to enter a name for their character. The method then sends this
     * modal to the user.
     * 
     * @param event The event that triggered this method.
     */
    private void handleCharacterNameAction(ButtonInteractionEvent event){
        TextInput nameInput = TextInput.create("dnd5e-name", "Character Name", TextInputStyle.SHORT)
            .setRequired(true)
            .setRequiredRange(1, 100)
            .setPlaceholder("Name of the Character")
            .build();
        
        Modal modal = Modal.create("dnd5e-name-modal", "Character Name")
            .addComponents(ActionRow.of(nameInput))
            .build();

        event.replyModal(modal).queue();
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-url". This method creates a modal with a text input for the user
     * to enter a URL for their character's image. The method then sends this
     * modal to the user.
     *
     * @param event The event that triggered this method.
     */
    private void handleCharacterImageDisplay(ButtonInteractionEvent event){
        TextInput urlInput = TextInput.create("dnd5e-url", "Character Image", TextInputStyle.SHORT)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("URL for character image")
            .build();
        
        Modal modal = Modal.create("dnd5e-image-modal", "Character Image")
            .addComponents(ActionRow.of(urlInput))
            .build();

        event.replyModal(modal).queue();
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-desc". This method creates a modal with a text input for the user
     * to enter their character's physical description. The method then sends
     * this modal to the user.
     * 
     * @param event The event that triggered this method.
     */
    private void handleCharacterDescription(ButtonInteractionEvent event){
        TextInput urlInput = TextInput.create("dnd5e-desc", "Character Description", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Physical description of character")
            .build();
        
        Modal modal = Modal.create("dnd5e-desc-modal", "Character Description")
            .addComponents(ActionRow.of(urlInput))
            .build();

        event.replyModal(modal).queue();
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-details". This method creates a modal with text inputs for the user
     * to enter their character's personality traits, ideals, bonds, and flaws.
     * The method then sends this modal to the user.
     * 
     * @param event The event that triggered this method.
     */
    private void handleCharacterDetails(ButtonInteractionEvent event){
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

        event.replyModal(modal).queue();
    }

    /**
     * Handles the button interaction for the button with the custom ID
     * "dnd5e-backstory". The method creates a modal with a text input for the
     * user to enter their character's backstory. The method then sends this
     * modal to the user.
     * 
     * @param event The event that triggered this method.
     */
    private void handleCharacterBackstory(ButtonInteractionEvent event){
        TextInput backstoryInput = TextInput.create("dnd5e-backstory", "Character Backstory", TextInputStyle.PARAGRAPH)
            .setRequired(true)
            .setMinLength(1)
            .setPlaceholder("Character backstory")
            .build();
        
        Modal modal = Modal.create("dnd5e-backstory-modal", "Character Description")
            .addComponents(ActionRow.of(backstoryInput))
            .build();

        event.replyModal(modal).queue();
    }

    /**
     * Listens for button interactions and executes the associated logic.
     * 
     * The method provides a default case to handle button interactions that are not recognized.
     * 
     * @param event The button interaction event.
     */
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event){
        //if(!checkInteractionValidity(event)) return;
        //We will have to check this later during modal interaction handling in order for modals to work

        String buttonId = event.getComponentId(); // The ID of the button clicked

        String game = "";
        String sel = "";

        switch (buttonId) {
            case "character-name-action":
                // Handle character name action
                handleCharacterNameAction(event);
                game = "dnd5e";
                sel = "name";
                break;
            case "character-image-display":
                // Handle character image display action
                handleCharacterImageDisplay(event);
                game = "dnd5e";
                sel = "image";
                break;
            case "character-description":
                // Handle character description action
                handleCharacterDescription(event);
                game = "dnd5e";
                sel = "description";
                break;
            case "character-details":
                // Handle character details action
                handleCharacterDetails(event);
                game = "dnd5e";
                sel = "details";
                break;
            case "character-backstory":
                // Handle character backstory action
                handleCharacterBackstory(event);
                game = "dnd5e";
                sel = "backstory";
                break;
            default:
                event.getHook().sendMessage("Button interaction not recognized.").queue();
                break;
        }
        String name = event.getUser().getName();
        Long id = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        String s = "User named " + name + " with ID " + id + " made "+game+" "+sel+" selection in thread with ID "+threadID;
        DataOutputter.logMessage(s, DataOutputter.INFO);
    }

    /**
     * Handles the logic for StringSelectInteractionEvents where the select menu ID is "game-selection".
     * 
     * The method disables the string select menu, sends a confirmation message, and then sends the embeds and action
     * rows associated with the selected game.
     * 
     * @param event The StringSelectInteractionEvent.
     */
    private void handleGameSelectionMenu(StringSelectInteractionEvent event) {
        GuildMessageChannel channel = event.getChannel().asGuildMessageChannel();
        String selectedOption = event.getValues().get(0); // Get the selected value
        Message eventMessage = event.getMessage();
        String charName = "Unnamed";
        String gameName = "";

        // disable the string select menu
        List<LayoutComponent> disabledComponents = eventMessage.getComponents().stream()
            .map(component -> component.asDisabled())
            .collect(Collectors.toList());
        eventMessage.editMessageComponents(disabledComponents).queue();

        switch (selectedOption) {
            case "dnd5e":
                // Create character
                PlayerCharacters pcs = PlayerCharacters.getInstance();
                Long playerID = event.getUser().getIdLong();
                pcs.addCharacter(playerID, new TTRPGChar(playerID));

                // Wait for complete to make sure the character exists before allowing interactions with it
                event.getHook().sendMessage("Selected: " + selectedOption).complete();

                List<EmbedWrapper> embedWrappers = EmbedRetriever.getDnd5eCreationEmbeds();

                for (EmbedWrapper wrapper : embedWrappers) {
                    MessageCreateAction messageAction = channel.sendMessageEmbeds(wrapper.getEmbedBuilder().build());

                    // Add action rows (components) if available
                    for (ActionRow actionRow : wrapper.getComponents()) {
                        messageAction = messageAction.addActionRow(actionRow.getComponents());
                    }

                    // Queue the message to send it
                    messageAction.queue();
                }

                gameName = "D&D5E";
                break;
            default:
                event.getHook().sendMessage("Select menu interaction not recognized.").queue();
                break;
        }

        // Check if the channel is a private thread
        if (channel instanceof ThreadChannel) {
            // Rename the private thread
            ((ThreadChannel) channel).getManager().setName(gameName+" "+charName+" Character Creation").queue();
        }
    }

    private void handleSexSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement sex selection for dnd5e
    }

    private void handleAlignmentSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement alignment selection for dnd5e
    }

    private void handleRaceSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement race selection for dnd5e
    }

    private void handleClassSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement class selection for dnd5e
    }

    private void handleBackgroundSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement background selection for dnd5e
    }

    private void handleAttributeMethodSelectionMenu(StringSelectInteractionEvent event) {
        //TODO: implement attribute selection for dnd5e
    }

    /**
     * Listens for string select interactions and executes the associated logic.
     * 
     * The method provides a default case to handle string select interactions that are not recognized.
     * 
     * @param event The string select interaction event.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event){
        if(!checkInteractionValidity(event)) return; // Responds to event

        String selectMenuId = event.getComponentId(); // The ID of the select menu interacted with

        String game = "";
        String sel = "";

        switch(selectMenuId) {
            case "game-selection":
                handleGameSelectionMenu(event);
                sel = "game";
                break;
            case "sex-selection":
                handleSexSelectionMenu(event);
                game = "dnd5e";
                sel = "sex";
                break;
            case "alignment-selection":
                handleAlignmentSelectionMenu(event);
                game = "dnd5e";
                sel = "alignment";
                break;
            case "race-selection":
                handleRaceSelectionMenu(event);
                game = "dnd5e";
                sel = "race";
                break;
            case "class-selection":
                handleClassSelectionMenu(event);
                game = "dnd5e";
                sel = "class";
                break;
            case "background-selection":
                handleBackgroundSelectionMenu(event);
                game = "dnd5e";
                sel = "background";
                break;
            case "attribute-method-selection":
                handleAttributeMethodSelectionMenu(event);
                game = "dnd5e";
                sel = "attribute method";
                break;
            default:
                event.getHook().sendMessage("Select menu interaction not recognized.").queue();
                break;
        }
        String name = event.getUser().getName();
        Long id = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        String s = "User named " + name + " with ID " + id + " made "+game+" "+sel+" selection in thread with ID "+threadID;
        DataOutputter.logMessage(s, DataOutputter.INFO);
    }
}
