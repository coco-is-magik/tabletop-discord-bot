package cocoismagik.interactables;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class EmbedRetriever {

    public static List<EmbedWrapper> getCharacterCreationInitialEmbeds() {
        List<EmbedWrapper> embedList = new ArrayList<>();

        EmbedBuilder gameSelectionEmbed = new EmbedBuilder();
        gameSelectionEmbed.setTitle("Select Game");
        gameSelectionEmbed.setDescription("Select a game from the dropdown below.");

        ActionRow selectMenu = ActionRow.of(
                net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("game-selection")
                        .setPlaceholder("Select game")
                        .addOption("D&D 5th Edition", "dnd5e")
                        .build()
        );

        // Create an EmbedWrapper instance and add it to the list
        embedList.add(new EmbedWrapper(gameSelectionEmbed, List.of(selectMenu)));

        return embedList;
    }

    public static List<EmbedWrapper> getCharacterCreationEmbeds() {
        List<EmbedWrapper> embedList = new ArrayList<>();

        // Step 1: Choose Character Race
        EmbedBuilder raceEmbed = new EmbedBuilder();
        raceEmbed.setTitle("Step 1: Choose Character Race");
        raceEmbed.setDescription("Select your race from the dropdown below.");
        raceEmbed.setColor(Color.BLUE);

        ActionRow selectMenu = ActionRow.of(
                net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("race-selection")
                        .setPlaceholder("Select character race")
                        .addOption("Placeholder 1", "placeholder1")
                        .addOption("Placeholder 2", "placeholder2")
                        .build()
        );

        // Create an EmbedWrapper instance and add it to the list
        embedList.add(new EmbedWrapper(raceEmbed, List.of(selectMenu)));

        // Additional steps for character creation can be added here similarly

        return embedList;
    }
}
