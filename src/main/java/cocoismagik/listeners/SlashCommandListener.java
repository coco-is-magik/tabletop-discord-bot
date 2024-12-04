package cocoismagik.listeners;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    /**
     * Listens for slash commands and executes the associated logic.
     *
     * If the slash command is not recognized, it will reply with "Unrecognized command: <command name>".
     *
     * @param event The slash command interaction event.
     */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        switch(event.getName()) {
            case "ping":
                cocoismagik.commands.slash.global.Ping.commandLogic(event);
                break;
            case "testcommanddefault":
                cocoismagik.commands.slash.test.TestCommandDefault.commandLogic(event);
                break;
            default:
                event.reply("Unrecognized command: " + event.getName()).queue();
                break;
        }
    }
}
