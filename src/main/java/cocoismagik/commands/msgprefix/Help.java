package cocoismagik.commands.msgprefix;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help {
    public static void commandLogic(MessageReceivedEvent event) {
        String s = "Available commands:\n" + "!help - Show available commands\n";
        String m = event.getAuthor().getAsMention();
        event.getAuthor().openPrivateChannel().queue(pc -> pc.sendMessage(s).queue(), t -> event.getChannel().sendMessage(m + "\n" + s).queue());
    }
}
