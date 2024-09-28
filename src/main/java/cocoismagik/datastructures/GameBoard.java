package cocoismagik.datastructures;

import java.util.HashMap;
import java.util.Map;
import cocoismagik.main.DataOutputter;

public class GameBoard {
    private Map<String, Space> spaces;

    public GameBoard() {
        this.spaces = new HashMap<>();
    }

    /**
     * Adds a new space to the game board.
     *
     * @param id the unique identifier of the space
     * @param data the data associated with the space
     */
    public void addSpace(String id, int data) {
        spaces.put(id, new Space(id, data));
    }

    /**
     * Creates an undirected connection between two spaces on the game board.
     *
     * @param id1 the ID of the first space
     * @param id2 the ID of the second space
     * @param distance the distance between the two spaces
     */
    public void addUndirectedConnection(String id1, String id2, int distance) {
        Space space1 = spaces.get(id1);
        Space space2 = spaces.get(id2);

        if(space1 != null && space2 != null) {
            space1.addNeighbor(id2, distance);
            space2.addNeighbor(id1, distance);
        } else {
            DataOutputter.logMessage("failed to create undirected connection between space 1: " + id1 + " and space 2: " + id2, DataOutputter.WARNING);
        }
    }

    /**
     * Creates a directed connection between two spaces on the game board.
     *
     * @param id1 the ID of the first space
     * @param id2 the ID of the second space
     * @param distance the distance between the two spaces
     */
    public void addDirectedConnection(String id1, String id2, int distance) {
        Space space1 = spaces.get(id1);
        Space space2 = spaces.get(id2);

        if(space1 != null && space2 != null) {
            space1.addNeighbor(id2, distance);
        } else {
            DataOutputter.logMessage("failed to create directed connection between space 1: " + id1 + " and space 2: " + id2, DataOutputter.WARNING);
        }
    }

    /**
     * Retrieves a Space object from the spaces HashMap based on the given ID.
     *
     * @param id the ID of the Space object to retrieve
     * @return the Space object with the given ID, or null if not found
     */
    public Space getSpace(String id) {
        return spaces.get(id);
    }
}
