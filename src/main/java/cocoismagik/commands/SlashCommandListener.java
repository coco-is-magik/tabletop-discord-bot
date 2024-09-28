package cocoismagik.commands;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        switch(event.getName()) {
            case "ping":
                Ping.commandLogic(event);
                break;
            case "dicegame":
                cocoismagik.commands.DiceGame.commandLogic(event);
                break;
            default:
                event.reply("Unrecognized command: " + event.getName()).queue();
                break;
        }
    }
}
