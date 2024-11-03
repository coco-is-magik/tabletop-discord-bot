package cocoismagik.interactables;

import java.util.List;
import javax.annotation.Nonnull;
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
     * Listens for string select interactions and executes the associated logic.
     * 
     * The method provides a default case to handle string select interactions that are not recognized.
     * 
     * @param event The string select interaction event.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event){
        String selectMenuId = event.getComponentId(); // The ID of the select menu interacted with

        // Game Selection
        if ("game-selection".equals(selectMenuId)) {
            event.deferReply().setEphemeral(true).queue();
            GuildMessageChannel channel = event.getChannel().asGuildMessageChannel();
            String selectedOption = event.getValues().get(0); // Get the selected value

            // disable the string select menu
            for(LayoutComponent component : event.getMessage().getComponents()) {
                event.editComponents(component.asDisabled()).queue();
            }

            switch (selectedOption) {
                case "dnd5e":
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
                    break;
                default:
                    event.reply("Select menu interaction not recognized.").setEphemeral(true).queue();
                    break;
            }
        } else {
            event.reply("Select menu interaction not recognized.").setEphemeral(true).queue();
        }
    }
}
