package cocoismagik.datastructures;

import java.util.HashMap;
import java.util.Map;

public class TTRPGChar {
    // Map for storing abilities (e.g., spells, special abilities, powers)
    private Map<String, Object> abilities;

    // Map for storing attributes (e.g., strength, dexterity, intelligence)
    private Map<String, Object> attributes;

    // Map for storing details (e.g., character name, race, class, background info)
    private Map<String, Object> details;

    // Map for storing statistics (e.g., hit points, level, armor class)
    private Map<String, Object> statistics;

    private Long playerID;
    private Long originThreadId;

    public TTRPGChar(Long playerID, Long originThreadId) {
        this.playerID = playerID;
        abilities = new HashMap<>();
        attributes = new HashMap<>();
        details = new HashMap<>();
        statistics = new HashMap<>();
        PlayerCharacters.getInstance().addCharacter(playerID, this);
    }

    // Methods for interacting with abilities
    public void addAbility(String abilityName, Object ability) {
        abilities.put(abilityName, ability);
    }

    public Object getAbility(String abilityName) {
        return abilities.get(abilityName);
    }

    public void removeAbility(String abilityName) {
        abilities.remove(abilityName);
    }

    // Methods for interacting with attributes
    public void addAttribute(String attributeName, Object attribute) {
        attributes.put(attributeName, attribute);
    }

    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    // Methods for interacting with details
    public void addDetail(String detailName, Object detail) {
        details.put(detailName, detail);
    }

    public Object getDetail(String detailName) {
        return details.get(detailName);
    }

    public void removeDetail(String detailName) {
        details.remove(detailName);
    }

    // Methods for interacting with statistics
    public void addStatistic(String statisticName, Object statistic) {
        statistics.put(statisticName, statistic);
    }

    public Object getStatistic(String statisticName) {
        return statistics.get(statisticName);
    }

    public void removeStatistic(String statisticName) {
        statistics.remove(statisticName);
    }

    public Long getPlayerID() {
        return playerID;
    }

    public Long getOriginThreadId() {
        return originThreadId;
    }
}
