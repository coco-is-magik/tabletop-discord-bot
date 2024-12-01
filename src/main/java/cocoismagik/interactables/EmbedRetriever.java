package cocoismagik.interactables;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class EmbedRetriever {

    /**
     * Retrieves the initial embeds for character creation, including the game selection dropdown.
     * 
     * @return a list of EmbedWrapper instances
     */
    public static List<EmbedWrapper> getCharacterCreationInitialEmbeds() {
        List<EmbedWrapper> embedList = new ArrayList<>();

        EmbedBuilder gameSelectionEmbed = new EmbedBuilder();
        gameSelectionEmbed.setTitle("Choose Game");
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

    /**
     * Retrieves the embeds for character creation in D&D 5th Edition, including character details, race selection, class selection, background selection, and attribute method selection.
     * 
     * @return a list of EmbedWrapper instances
     */
    public static List<EmbedWrapper> getDnd5eCreationEmbeds() {
        List<EmbedWrapper> embedList = new ArrayList<>();

        // Step 0: Choose character details
        ActionRow buttonRow1 = ActionRow.of(
            Button.secondary("character-name-action", "Name"),
            Button.secondary("character-image-display", "Image URL"),
            Button.secondary("character-description", "Physical Description ")
        );

        ActionRow buttonRow2 = ActionRow.of(
            Button.secondary("character-backstory", "Write Character Backstory"),
            Button.secondary("character-details", "Details and Traits"),
            Button.primary("character-randomize", "Randomize")
        );

        ActionRow sexSelectMenu = ActionRow.of(
            net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("sex-selection")
                        .setPlaceholder("Select character sex")
                        .addOption("Male", "male")
                        .addOption("Female", "female")
                        .build()
        );

        ActionRow alignmentSelectMenu = ActionRow.of(
            net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("alignment-selection")
                    .setPlaceholder("Select character alignment")
                    .addOption("Lawful Good", "lawful-good")
                    .addOption("Neutral Good", "neutral-good")
                    .addOption("Chaotic Good", "chaotic-good")
                    .addOption("Lawful Neutral", "lawful-neutral")
                    .addOption("Neutral", "neutral")
                    .addOption("Chaotic Neutral", "chaotic-neutral")
                    .addOption("Lawful Evil", "lawful-evil")
                    .addOption("Neutral Evil", "neutral-evil")
                    .addOption("Chaotic Evil", "chaotic-evil")
                    .build()
        );

        // Step 1: Choose Character Race
        ActionRow raceSelectMenu = ActionRow.of(
                net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("race-selection")
                        .setPlaceholder("Select character race")
                        .addOption("Dragonborn", "dragonborn")
                        .addOption("Dwarf (Hill)", "dwarf-hill")
                        .addOption("Dwarf (Mountain)", "dwarf-mountain")
                        .addOption("Elf (Drow)", "elf-drow")
                        .addOption("Elf (High)", "elf-high")
                        .addOption("Elf (Wood)", "elf-wood")
                        .addOption("Gnome (Forest)", "gnome-forest")
                        .addOption("Gnome (Rock)", "gnome-rock")
                        .addOption("Half-Elf", "half-elf")
                        .addOption("Half-Orc", "half-orc")
                        .addOption("Halfling (Lightfoot)", "halfling-lightfoot")
                        .addOption("Halfling (Stout)", "halfling-stout")
                        .addOption("Human (Base)", "human-base")
                        .addOption("Human (Variant)", "human-variant")
                        .addOption("Tiefling", "tiefling")
                        .build()
        );

        // Step 2: Choose Character Class
        ActionRow classSelectMenu = ActionRow.of(
            net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("class-selection")
                    .setPlaceholder("Select character class")
                    .addOption("Barbarian", "barbarian")
                    .addOption("Bard", "bard")
                    .addOption("Cleric", "cleric")
                    .addOption("Druid", "druid")
                    .addOption("Fighter", "fighter")
                    .addOption("Monk", "monk")
                    .addOption("Paladin", "paladin")
                    .addOption("Ranger", "ranger")
                    .addOption("Rogue", "rogue")
                    .addOption("Sorcerer", "sorcerer")
                    .addOption("Warlock", "warlock")
                    .addOption("Wizard", "wizard")
                    .build()
        );

        // Step 3: Choose Character Background
        ActionRow backgroundSelectMenu = ActionRow.of(
            net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("background-selection")
                    .setPlaceholder("Select character background")
                    .addOption("Acolyte", "acolyte")
                    .addOption("Charlatan", "charlatan")
                    .addOption("Criminal", "criminal")
                    .addOption("Entertainer", "entertainer")
                    .addOption("Folk Hero", "folk-hero")
                    .addOption("Guild Artisan", "guild-artisan")
                    .addOption("Hermit", "hermit")
                    .addOption("Noble", "noble")
                    .addOption("Outlander", "outlander")
                    .addOption("Sage", "sage")
                    .addOption("Sailor", "sailor")
                    .addOption("Soldier", "soldier")
                    .addOption("Urchin", "urchin")
                    .build()
        );

        // Step 4: Choose Attribute Method
        ActionRow attributeSelectMenu = ActionRow.of(
            net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create("attribute-selection")
                    .setPlaceholder("Select attribute method")
                    .addOption("Point Buy", "point-buy")
                    .addOption("Standard Array", "standard-array")
                    .addOption("4d6 Drop Lowest", "4d6-drop-lowest")
                    .build()
        );

        EmbedBuilder characterDetailsDisplayEmbed = new EmbedBuilder();
        characterDetailsDisplayEmbed.setTitle("In Progress Character Sheet");
        characterDetailsDisplayEmbed.setDescription("As you make choices, this will get updated!");
        embedList.add(new EmbedWrapper(characterDetailsDisplayEmbed, List.of()));

        // Create an EmbedWrapper instance and add it to the list
        EmbedBuilder characterDetailsEmbed = new EmbedBuilder();
        characterDetailsEmbed.setTitle("Set Character Details");
        characterDetailsEmbed.setDescription("Click each button for a prompt to enter your character's details. "
        +"\n\nFor image URLs, some external resources might work, but if it's getting rejected you can upload a picture in this chat and "
        +"copy that link instead. To do that you will need to download the image and then upload it from your computer, not just paste the "
        +"link in the chat."
        +"\n\nTo randomize your character details, just hit the randomize button and it will generate a random details for you, but not an "
        +"image, you'll need to provide that link yourself still.");

        embedList.add(new EmbedWrapper(characterDetailsEmbed, List.of(
            buttonRow1,
            buttonRow2
        )));

        EmbedBuilder number1DetailsBuilder = new EmbedBuilder();
        number1DetailsBuilder.setTitle("Select Sex, Alignment, and Background");
        number1DetailsBuilder.setDescription("You will be prompted for further details after you make selections.");
        embedList.add(new EmbedWrapper(number1DetailsBuilder, List.of(
            sexSelectMenu,
            alignmentSelectMenu,
            backgroundSelectMenu
        )));
         
        EmbedBuilder number2DetailsBuilder = new EmbedBuilder();
        number2DetailsBuilder.setTitle("Select Race, Class, and Attribute Method");
        number2DetailsBuilder.setDescription("You will be prompted for further details after you make selections.");
        embedList.add(new EmbedWrapper(number2DetailsBuilder, List.of(
            raceSelectMenu,
            classSelectMenu,
            attributeSelectMenu
        )));

        // From here, we need to know class and background to prompt for skills
        // The attribute selection method for assigning attributes
        // The class for spell selection
        // The race for language selection
        // So this flow must continue past this point as the user makes choices

        return embedList;
    }
}
