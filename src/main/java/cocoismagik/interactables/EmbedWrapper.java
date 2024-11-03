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

    
    /**
     * Gets the embed builder of the EmbedWrapper object.
     * 
     * @return the embed builder
     */
    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    
    /**
     * Gets the list of components associated with the EmbedWrapper object.
     * 
     * @return the list of components
     */
    public List<ActionRow> getComponents() {
        return components;
    }

    
    /**
     * Creates an EmbedWrapper containing an embed for character race selection and
     * an action row containing a string select menu with placeholder options.
     * 
     * @return an EmbedWrapper for character race selection
     */
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
