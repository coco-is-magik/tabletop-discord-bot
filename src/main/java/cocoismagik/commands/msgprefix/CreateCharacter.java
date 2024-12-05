package cocoismagik.commands.msgprefix;

import java.util.ArrayList;
import java.util.List;

import cocoismagik.datastructures.ThreadManagementTracker;
import cocoismagik.interactables.EmbedWrapper;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class CreateCharacter {

    /**
     * Retrieves the initial embeds for character creation, including the game selection dropdown.
     * 
     * @return a list of EmbedWrapper instances
     */
    public static List<EmbedWrapper> getCharacterCreationInitialEmbeds() {
        List<EmbedWrapper> embedList = new ArrayList<>();

        EmbedBuilder gameSelectionEmbed = new EmbedBuilder();
        gameSelectionEmbed.setTitle("Choose Game");
        gameSelectionEmbed.setDescription("Select a game from the dropdown below.");

        ActionRow selectMenu = ActionRow.of(
                net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("game-selection")
                        .setPlaceholder("Select game")
                        .addOption("D&D 5th Edition", "dnd5e")
                        .build()
        );

        // Create an EmbedWrapper instance and add it to the list
        embedList.add(new EmbedWrapper(gameSelectionEmbed, List.of(selectMenu)));

        return embedList;
    }

    public static void commandLogic(MessageReceivedEvent event){
        Message message = event.getMessage();
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = event.getChannel().asTextChannel();
            channel.createThreadChannel("Private Thread", true)
                    .setAutoArchiveDuration(ThreadChannel.AutoArchiveDuration.TIME_24_HOURS)
                    .queue(thread -> {
                        // Associate the thread with the user, and ensure something very strange hasnt happened
                        try{
                            ThreadManagementTracker.addThreadOwnership(event.getAuthor().getIdLong(), thread.getIdLong());
                        } catch (IllegalArgumentException e) {
                            DataOutputter.logMessage(e.getMessage(), DataOutputter.ERROR);
                            return;
                        }

                        // Send an initial message in the thread
                        thread.sendMessage("This is a private thread for character creation.").queue();

                        List<EmbedWrapper> embedWrappers = getCharacterCreationInitialEmbeds();

                        for (EmbedWrapper wrapper : embedWrappers) {
                            MessageCreateAction messageAction = thread.sendMessageEmbeds(wrapper.getEmbedBuilder().build());

                            // Add action rows (components) if available
                            for (ActionRow actionRow : wrapper.getComponents()) {
                                messageAction = messageAction.addActionRow(actionRow.getComponents());
                            }

                            // Queue the message to send it
                            messageAction.queue();
                        }

                        // Invite the original user who invoked the command
                        thread.addThreadMember(event.getAuthor()).queue(
                            success -> {
                                event.getChannel().sendMessage("You have been added to the private thread!").queue();
                                String name = event.getAuthor().getName();
                                Long id = event.getAuthor().getIdLong();
                                String s = "Made charcreate thread for user named " + name + " with id " + id;
                                DataOutputter.logMessage(s, DataOutputter.INFO);
                            },
                            failure -> {
                                //FIXME: ping a moderator
                                event.getChannel().sendMessage("Failed to add you to the private thread.").queue();
                                String name = event.getAuthor().getName();
                                Long id = event.getAuthor().getIdLong();
                                String s = "Failed to make charcreate thread for user named " + name + " with id " + id;
                                DataOutputter.logMessage(s, DataOutputter.ERROR);
                            }
                        );
                    });
        } else {
            message.reply("This command can only be used in a server channel.").queue();
        }
    }
}
