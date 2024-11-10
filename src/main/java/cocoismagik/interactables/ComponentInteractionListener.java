package cocoismagik.interactables;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import cocoismagik.datastructures.PlayerCharacters;
import cocoismagik.datastructures.TTRPGChar;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class ComponentInteractionListener extends ListenerAdapter {
    /**
     * Listens for button interactions and executes the associated logic.
     * 
     * The method provides a default case to handle button interactions that are not recognized.
     * 
     * @param event The button interaction event.
     */
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event){
        String buttonId = event.getComponentId(); // The ID of the button clicked

        switch (buttonId) {
            // Add cases for other button IDs as needed
            default:
                event.reply("Button interaction not recognized.").setEphemeral(true).queue();
                break;
        }
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
        event.deferReply().setEphemeral(true).queue();
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
        } else {
            // Log or handle the case where it's not a private thread
            System.out.println("Not in a private thread, cannot rename.");
        }
    }

    private void handleSexSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
    }

    private void handleAlignmentSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
    }

    private void handleRaceSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
    }

    private void handleClassSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
    }

    private void handleBackgroundSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
    }

    private void handleAttributeMethodSelectionMenu(StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
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
        String selectMenuId = event.getComponentId(); // The ID of the select menu interacted with

        switch(selectMenuId) {
            case "game-selection":
                handleGameSelectionMenu(event);
                break;
            case "sex-selection":
                handleSexSelectionMenu(event);
                break;
            case "alignment-selection":
                handleAlignmentSelectionMenu(event);
                break;
            case "race-selection":
                handleRaceSelectionMenu(event);
                break;
            case "class-selection":
                handleClassSelectionMenu(event);
                break;
            case "background-selection":
                handleBackgroundSelectionMenu(event);
                break;
            case "attribute-method-selection":
                handleAttributeMethodSelectionMenu(event);
                break;
            default:
                event.reply("Select menu interaction not recognized.").setEphemeral(true).queue();
                break;
        }
    }
}
