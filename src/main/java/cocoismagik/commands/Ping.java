package cocoismagik.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ping{
    public static void commandLogic(SlashCommandInteractionEvent event) {
        event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
        boolean optionExists = event.getOption("message") != null;
        String response = (optionExists) ? event.getOption("message").getAsString() : "Pong!";
        event.getHook().sendMessage(response).queue();
    }
}
