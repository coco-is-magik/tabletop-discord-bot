package cocoismagik.datastructures;

import java.util.HashMap;
import java.util.Map;

public class Space {
    private String id;
    private int data;
    private Map<String, Integer> neighbors;

    public Space(String id, int data) {
        this.id = id;
        this.data = data;
        this.neighbors = new HashMap<>();
    }

    /**
     * Adds a new neighbor to the Space object.
     *
     * @param id the unique identifier of the neighbor
     * @param data the data value associated with the neighbor
     */
    public void addNeighbor(String id, int data) {
        neighbors.put(id, data);
    }

    /**
     * Retrieves the unique identifier of the Space object.
     *
     * @return the unique identifier of the Space object
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the data value of the Space object.
     *
     * @return the data value of the Space object
     */
    public int getData() {
        return data;
    }

    /**
     * Sets the data value of the Space object.
     *
     * @param data the new data value
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * Returns the map of neighbors.
     *
     * @return the map of neighbors
     */
    public Map<String, Integer> getNeighbors() {
        return neighbors;
    }
}
