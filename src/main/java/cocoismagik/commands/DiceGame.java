package cocoismagik.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiceGame{
    public static void commandLogic(SlashCommandInteractionEvent event) {
        event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
        boolean optionExists = event.getOption("guess") != null;
        if (!optionExists) {
            event.getHook().sendMessage("You must guess a number!").queue();
            return;
        } else {
            event.getHook().sendMessage(cocoismagik.games.DiceGame.guess(event.getOption("guess").getAsInt()).getActionDescription()).queue();
            return;
        }
    }
}
