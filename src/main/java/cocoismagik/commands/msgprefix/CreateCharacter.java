package cocoismagik.commands.msgprefix;

import cocoismagik.datastructures.ThreadManagementTracker;
import cocoismagik.main.DataOutputter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class CreateCharacter {

    /**
     * Sets up a new character creation thread by associating it with a user and
     * adding the user to the thread. It then sends a game selection embed to the
     * thread, allowing the user to choose a game for character creation.
     *
     * @param thread      the thread channel to set up
     * @param threadOwner the owner of the thread to associate with the thread
     * @throws IllegalArgumentException if the thread ownership cannot be established
     */
    private static void setupThread(ThreadChannel thread, User threadOwner) {
        // Need to associate thread with user or the rest of the create character process will fail
        try{
            ThreadManagementTracker.addThreadOwnership(threadOwner.getIdLong(), thread.getIdLong());
        } catch (IllegalArgumentException e) {
            DataOutputter.logMessage(e.getMessage(), DataOutputter.ERROR);
            return;
        }

        // Add the user to the thread
        thread.addThreadMember(threadOwner).complete();

        //FIXME: this check is broken, it detects failure when the user is already in the thread
        /* 
        // Check the user was added to thread
        if (thread.getThreadMember(threadOwner) != null) {
            String s = "Made charcreate thread for user " + threadOwner.getName() + " (" + threadOwner.getIdLong() + ")";
            DataOutputter.logMessage(s, DataOutputter.INFO);
        } else {
            String s = "Couldn't make charcreate thread for user " + threadOwner.getName() + " (" + threadOwner.getIdLong() + ")";
            DataOutputter.logMessage(s, DataOutputter.ERROR);
        }
        */

        // Send the game selection embed so the player can choose what kind of character to make
        EmbedBuilder gameSelectionEmbed = new EmbedBuilder();
        gameSelectionEmbed.setTitle("Choose Game");
        gameSelectionEmbed.setDescription("Select a game from the dropdown below.");

        ActionRow selectMenu = ActionRow.of(
            StringSelectMenu.create("game-selection")
                .setPlaceholder("Select game")
                .addOption("D&D 5th Edition", "dnd5e")
                .build()
        );

        MessageCreateAction messageAction = thread.sendMessageEmbeds(gameSelectionEmbed.build());
        messageAction = messageAction.addActionRow(selectMenu.getComponents());
        messageAction.queue();
    }

    /**
     * Handles the "!createcharacter" command issued by a user in a text channel.
     * 
     * This method checks if the command is sent from a text channel. If it is, it
     * creates a private thread for the user to start the character creation process
     * and sets up the thread with necessary configurations. If the command is not
     * in a text channel, it sends a reply to the user indicating that the command
     * is invalid in the current location.
     * 
     * @param event The event containing the message and channel information.
     */
    public static void commandLogic(MessageReceivedEvent event){
        // Get the message received
        Message message = event.getMessage();

        // Check if the message was sent in a text channel
        if (event.isFromType(ChannelType.TEXT)) {
            // Create the private thread for the user to create their character
            ThreadChannel thread = event.getChannel().asTextChannel().createThreadChannel("Private Thread", true).complete();
            setupThread(thread, event.getAuthor());
        } else {
            message.reply("Invalid location for command, please run the command in a text channel").queue();
            return;
        }
    }
}
