package cocoismagik.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cocoismagik.main.DataOutputter;

public class PlayerCharacters {
    private static PlayerCharacters instance;

    private Map<Long, List<TTRPGChar>> characters;

    private PlayerCharacters() {
        characters = new HashMap<>();
    }

    /**
     * Public static method to get the instance of the class.
     * 
     * This method ensures that only one instance of the class is created, and returns that instance.
     * 
     * @return the instance of the class
     */
    
    public static PlayerCharacters getInstance() {
        if (instance == null) {
            instance = new PlayerCharacters();
        }
        return instance;
    }

    /**
     * Adds a character to the player's list of characters.
     * 
     * If the player does not have a list of characters yet, it will be created.
     * 
     * @param playerID the ID of the player to add the character to
     * @param character the character to add
     */
    public void addCharacter(Long playerID, TTRPGChar character) {
        //TODO: connect to database
        DataOutputter.logMessage("Adding character to player "+playerID+": "+character.toString(), DataOutputter.INFO);
        characters.computeIfAbsent(playerID, k -> new ArrayList<>()).add(character);
    }

    /**
     * Retrieves the list of characters associated with a given player ID.
     * 
     * @param playerID the ID of the player to retrieve the characters for
     * @return the list of characters, or null if no characters exist for the given player ID
     */
    public List<TTRPGChar> getCharacters(Long playerID) {
        return characters.get(playerID);
    }
}
