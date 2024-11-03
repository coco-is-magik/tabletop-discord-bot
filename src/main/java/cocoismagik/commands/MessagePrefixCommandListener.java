package cocoismagik.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessagePrefixCommandListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event){
        // Ensure the bot does not respond to its own messages
        if (event.getAuthor().isBot()) return;
        
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String prefix = "!";
        String userMention = event.getAuthor().getAsMention();

        // Check if the message starts with the prefix and handle it
        if (content.startsWith(prefix)) {
            String command = content.substring(prefix.length()).trim().split(" ")[0].toLowerCase();

            switch (command) {
                // Example commands
                /*
                // 1. Command that replies to the message
                case "reply":
                    message.reply("This is a reply to your message!").queue();
                    break;

                // 2. Command that mentions the user
                case "mention":
                    event.getChannel().sendMessage(userMention + " This message mentions you directly!").queue();
                    break;

                // 3. Command that starts a private thread (must be within a channel that supports threads)
                case "privatethread":
                    if (event.isFromType(ChannelType.TEXT)) {
                        TextChannel channel = event.getChannel().asTextChannel();
                        channel.createThreadChannel("Private Thread", true)
                               .setAutoArchiveDuration(ThreadChannel.AutoArchiveDuration.TIME_24_HOURS)
                               .queue(thread -> {
                                   // Send an initial message in the thread
                                   thread.sendMessage("This is a private thread for continued discussion.").queue();

                                   // Invite the original user who invoked the command
                                   thread.addThreadMember(event.getAuthor()).queue(
                                       success -> event.getChannel().sendMessage("You have been added to the private thread!").queue(),
                                       failure -> event.getChannel().sendMessage("Failed to add you to the private thread.").queue()
                                   );
                               });
                    } else {
                        message.reply("This command can only be used in a server channel.").queue();
                    }
                    break;

                // 4. Command that starts a public thread
                case "publicthread":
                    if (event.isFromType(ChannelType.TEXT)) {
                        TextChannel channel = event.getChannel().asTextChannel();
                        channel.createThreadChannel("Public Discussion Thread", false)
                               .setAutoArchiveDuration(ThreadChannel.AutoArchiveDuration.TIME_24_HOURS)
                               .queue(thread -> thread.sendMessage("This is a public thread for everyone to participate in.").queue());
                    } else {
                        message.reply("This command can only be used in a server channel.").queue();
                    }
                    break;

                // 5. Command that sends a direct message to the user
                case "dm":
                    event.getAuthor().openPrivateChannel()
                        .queue(privateChannel -> privateChannel.sendMessage("Hello! This is a direct message from the bot.").queue(),
                               throwable -> event.getChannel().sendMessage(userMention + " I couldn't send you a direct message!").queue());
                    break;
                */
                case "createcharacter":
                    if (event.isFromType(ChannelType.TEXT)) {
                        TextChannel channel = event.getChannel().asTextChannel();
                        channel.createThreadChannel("Private Thread", true)
                               .setAutoArchiveDuration(ThreadChannel.AutoArchiveDuration.TIME_24_HOURS)
                               .queue(thread -> {
                                   // Send an initial message in the thread
                                   thread.sendMessage("This is a private thread for character creation.").queue();

                                   // Invite the original user who invoked the command
                                   thread.addThreadMember(event.getAuthor()).queue(
                                       success -> event.getChannel().sendMessage("You have been added to the private thread!").queue(),
                                       failure -> event.getChannel().sendMessage("Failed to add you to the private thread.").queue()
                                   );
                               });
                    } else {
                        message.reply("This command can only be used in a server channel.").queue();
                    }
                    break;
                case "help":
                    String helpMessage = "Available commands:\n" +
                            "!help - Show available commands\n";
                    event.getAuthor().openPrivateChannel()
                        .queue(privateChannel -> privateChannel.sendMessage(helpMessage).queue(),
                               throwable -> event.getChannel().sendMessage(userMention + "\n" + helpMessage).queue());
                    break;
                default:
                    message.reply("Unknown command. Type !help for a list of available commands.").queue();
                    break;
            }
        }
    }
}
