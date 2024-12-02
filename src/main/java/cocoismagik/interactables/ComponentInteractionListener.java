package cocoismagik.interactables;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import cocoismagik.datastructures.CharacterCreationThreadButtonsEmbedTracker;
import cocoismagik.datastructures.CharacterCreationThreadDetailsEmbedTracker;
import cocoismagik.datastructures.PlayerCharacters;
import cocoismagik.datastructures.TTRPGChar;
import cocoismagik.datastructures.ThreadOwnershipTracker;
import cocoismagik.games.DND5eCharacterCreation;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
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
    @SuppressWarnings("null") // This is a false positive, the compiler is too fucking stupid to realize we return early in this case
    private boolean checkInteractionValidity(GenericInteractionCreateEvent event){
        ModalInteractionEvent m = null;
        GenericComponentInteractionCreateEvent g = null;
        if (event instanceof GenericComponentInteractionCreateEvent) {
            g = (GenericComponentInteractionCreateEvent) event;
        } else if (event instanceof ModalInteractionEvent) {
            m = (ModalInteractionEvent) event;
        } else {
            return false;
        }

        ((m != null) ? m : g).deferReply().setEphemeral(true).queue();

        long userId = event.getUser().getIdLong();

        if (!ThreadOwnershipTracker.ownsThread(userId, event.getChannelIdLong())) {
            String s = "User named "+event.getUser().getName()+" with id "+event.getUser().getIdLong()
                        +" attempted to interact with thread "+event.getChannelIdLong()+" but does not own it.";
            DataOutputter.logMessage(s, DataOutputter.INFO);
            ((m != null) ? m : g).getHook().sendMessage("You cannot interact with these components beccause this is not your thread.").queue();
            return false;
        }

        
        //((m != null) ? m : g).getChannel().asGuildMessageChannel(); my beloved...
        // It greatly fucking annoys me that the compiler cannot infer the type unless I do it this way
        GuildMessageChannel channel;
        if (m != null) {
            channel = m.getChannel().asGuildMessageChannel();
        } else {
            channel = g.getChannel().asGuildMessageChannel();
        }

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
                DND5eCharacterCreation.handleCharacterNameAction(event);
                game = "dnd5e";
                sel = "name";
                break;
            case "character-image-display":
                // Handle character image display action
                DND5eCharacterCreation.handleCharacterImageDisplay(event);
                game = "dnd5e";
                sel = "image";
                break;
            case "character-description":
                // Handle character description action
                DND5eCharacterCreation.handleCharacterDescription(event);
                game = "dnd5e";
                sel = "description";
                break;
            case "character-details":
                // Handle character details action
                DND5eCharacterCreation.handleCharacterDetails(event);
                game = "dnd5e";
                sel = "details";
                break;
            case "character-backstory":
                // Handle character backstory action
                DND5eCharacterCreation.handleCharacterBackstory(event);
                game = "dnd5e";
                sel = "backstory";
                break;
            case "character-randomize":
                // Handle character randomization action
                DND5eCharacterCreation.handleCharacterRandomization(event);
                game = "dnd5e";
                sel = "randomize";
            default:
                event.getHook().sendMessage("Button interaction not recognized.").queue();
                DataOutputter.logMessage("Button interaction called "+buttonId+" not recognized.", DataOutputter.WARNING);
                return;
        }
        String name = event.getUser().getName();
        Long id = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        String s = "User named " + name + " with ID " + id + " started "+game+" "+sel+" selection in thread with ID "+threadID;
        DataOutputter.logMessage(s, DataOutputter.INFO);
    }

    /**
     * Handles the logic for StringSelectInteractionEvents where the select menu ID is "game-selection".
     * 
     * The method disables the string select menu, sends a confirmation message, and then sends the embeds and action
     * rows associated with the selected game.
     * 
     * This method expects the interaction to have already been acknowledged with a deferReply().
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
                Long threadID = event.getChannel().getIdLong();
                pcs.addCharacter(playerID, new TTRPGChar(playerID, threadID));

                // Wait for complete to make sure the character exists before allowing interactions with it
                event.getHook().sendMessage("Selected: " + selectedOption).complete();

                List<EmbedWrapper> embedWrappers = EmbedRetriever.getDnd5eCreationEmbeds();

                for (int i = 0; i < embedWrappers.size(); i++) {
                    EmbedWrapper wrapper = embedWrappers.get(i);

                    MessageCreateAction messageAction = channel.sendMessageEmbeds(wrapper.getEmbedBuilder().build());

                    // Add action rows (components) if available
                    for (ActionRow actionRow : wrapper.getComponents()) {
                        messageAction = messageAction.addActionRow(actionRow.getComponents());
                    }

                    // Queue the message to send it
                    Message message = messageAction.complete();

                    if (i == 0) {
                        CharacterCreationThreadDetailsEmbedTracker.addMessage(message.getIdLong(), event.getChannel().getIdLong());
                    } else if (i == 1) {
                        CharacterCreationThreadButtonsEmbedTracker.addMessage(message.getIdLong(), event.getChannel().getIdLong());
                    }
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

        DataOutputter.logMessage("String select interaction called "+selectMenuId, DataOutputter.INFO);

        String game = "";
        String sel = "";

        switch(selectMenuId) {
            case "game-selection":
                handleGameSelectionMenu(event);
                sel = "game";
                break;
            case "sex-selection":
                DND5eCharacterCreation.handleSexSelectionMenu(event);
                game = "dnd5e";
                sel = "sex";
                break;
            case "alignment-selection":
                DND5eCharacterCreation.handleAlignmentSelectionMenu(event);
                game = "dnd5e";
                sel = "alignment";
                break;
            case "race-selection":
                DND5eCharacterCreation.handleRaceSelectionMenu(event);
                game = "dnd5e";
                sel = "race";
                break;
            case "class-selection":
                DND5eCharacterCreation.handleClassSelectionMenu(event);
                game = "dnd5e";
                sel = "class";
                break;
            case "background-selection":
                DND5eCharacterCreation.handleBackgroundSelectionMenu(event);
                game = "dnd5e";
                sel = "background";
                break;
            case "attribute-selection":
                DND5eCharacterCreation.handleAttributeMethodSelectionMenu(event);
                game = "dnd5e";
                sel = "attribute method";
                break;
            default:
                event.getHook().sendMessage("Select menu interaction not recognized.").queue();
                DataOutputter.logMessage("Select menu interaction called " + selectMenuId + " not recognized.", DataOutputter.WARNING);
                return;
        }
        String name = event.getUser().getName();
        Long id = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        String s = "User named " + name + " with ID " + id + " made "+game+" "+sel+" selection in thread with ID "+threadID;
        DataOutputter.logMessage(s, DataOutputter.INFO);
    }

    /**
     * Listens for modal interactions and executes the associated logic.
     * 
     * The method provides a default case to handle modal interactions that are not recognized.
     * 
     * @param event The modal interaction event.
     */
    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if(!checkInteractionValidity(event)) return; // Responds to event

        String modalId = event.getModalId(); // The ID of the modal interacted with

        String game = "";
        String sel = "";

        switch(modalId) {
            case "dnd5e-name-modal":
                DND5eCharacterCreation.handleCharacterName(event);
                game = "dnd5e";
                sel = "name";
                break;
            case "dnd5e-image-modal":
                DND5eCharacterCreation.handleCharacterImage(event);
                game = "dnd5e";
                sel = "image";
                break;
            case "dnd5e-desc-modal":
                DND5eCharacterCreation.handleCharacterDescription(event);
                game = "dnd5e";
                sel = "description";
                break;
            case "dnd5e-detail-modal":
                DND5eCharacterCreation.handleCharacterDetail(event);
                game = "dnd5e";
                sel = "details";
                break;
            case "dnd5e-backstory-modal":
                DND5eCharacterCreation.handleCharacterBackstory(event);
                game = "dnd5e";
                sel = "backstory";
                break;
            default:
                event.getHook().sendMessage("Modal interaction not recognized.").queue();
                DataOutputter.logMessage("Modal interaction called " + modalId + " not recognized.", DataOutputter.WARNING);
                return;
        }

        String name = event.getUser().getName();
        Long id = event.getUser().getIdLong();
        Long threadID = event.getChannel().getIdLong();
        String s = "User named " + name + " with ID " + id + " finished "+game+" "+sel+" selection in thread with ID "+threadID;
        DataOutputter.logMessage(s, DataOutputter.INFO);
    }
}
