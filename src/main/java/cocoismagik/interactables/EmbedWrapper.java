package cocoismagik.interactables;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class EmbedWrapper {
    private final EmbedBuilder embedBuilder;
    private final List<ActionRow> components;

    public EmbedWrapper(EmbedBuilder embedBuilder, List<ActionRow> components) {
        this.embedBuilder = embedBuilder;
        this.components = components;
    }

    // Method to get the built EmbedBuilder
    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    // Method to get the components
    public List<ActionRow> getComponents() {
        return components;
    }

    // Static method to create an EmbedWrapper for reuse
    public static EmbedWrapper createRaceSelectionEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Step 1: Choose Character Race");
        embed.setDescription("Select your race from the dropdown below.");
        embed.setColor(java.awt.Color.BLUE);

        ActionRow selectMenu = ActionRow.of(
                net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("race-selection")
                        .setPlaceholder("Select character race")
                        .addOption("Placeholder 1", "placeholder1")
                        .addOption("Placeholder 2", "placeholder2")
                        .build()
        );

        return new EmbedWrapper(embed, List.of(selectMenu));
    }
}
