package cocoismagik.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionResult implements GameResult {

    private String actionDescription;
    private boolean success;
    private int value;
    private Map<String, Object> addtionalData;
    private List<String> affectedEntities;

    public ActionResult(String actionDescription, boolean success, int value) {
        this.actionDescription = actionDescription;
        this.success = success;
        this.value = value;
        this.addtionalData = new HashMap<>();
        this.affectedEntities = new ArrayList<>();
    }

    /**
     * Adds a new entity to the list of affected entities.
     *
     * @param entity the entity to be added to the list of affected entities
     */
    public void addAffectedEntity(String entity) {
        affectedEntities.add(entity);
    }

    /**
     * Adds additional data to the ActionResult object.
     *
     * @param key the key for the additional data
     * @param value the value for the additional data
     */
    public void addAdditionalData(String key, Object value) {
        addtionalData.put(key, value);
    }

    /**
     * Retrieves the action description from the ActionResult object.
     *
     * @return The action description.
     */
    @Override
    public String getActionDescription() {
        return actionDescription;
    }

    /**
     * Checks if the action result was successful.
     *
     * @return true if the action result was successful, false otherwise
     */
    @Override
    public boolean isSuccess() {
        return success;
    }

    /**
     * Retrieves the value associated with the action result.
     *
     * @return The value of the action result.
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Retrieves the additional data associated with the action result.
     *
     * @return A map containing the additional data.
     */
    @Override
    public Map<String, Object> getAddtionalData() {
        return addtionalData;
    }

    /**
     * Retrieves the list of entities affected by the action result.
     *
     * @return The list of affected entities.
     */
    @Override
    public List<String> getAffectedEntities() {
        return affectedEntities;
    }
    
}
