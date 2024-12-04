package cocoismagik.commands.slash.test;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TestCommandDefault {
    public static void commandLogic(SlashCommandInteractionEvent event) {
        event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
        String response = "This is the default testing command response!";
        event.getHook().sendMessage(response).queue();
    }
}
