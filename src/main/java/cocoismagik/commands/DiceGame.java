package cocoismagik.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class DiceGame{
    public static void commandLogic(SlashCommandInteractionEvent event) {
        event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
        OptionMapping option = event.getOption("guess");
        if (option == null) {
            event.getHook().sendMessage("You must guess a number!").queue();
            return;
        } else {
            event.getHook().sendMessage(cocoismagik.games.DiceGame.guess(option.getAsInt()).getActionDescription()).queue();
            return;
        }
    }
}
