package cocoismagik.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class Ping{
    public static void commandLogic(SlashCommandInteractionEvent event) {
        event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
        OptionMapping option = event.getOption("message");
        String response = (option != null) ? option.getAsString() : "Pong!";
        event.getHook().sendMessage(response).queue();
    }
}
