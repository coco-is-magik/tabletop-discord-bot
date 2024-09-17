package cocoismagik.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rulebook {
    private String title;
    private Map<String, Entry> entries;

    public Rulebook(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.title = title;
        this.entries = new HashMap<>();
    }

    /**
     * Adds an entry to the rulebook.
     *
     * @param entry the entry to be added
     * @throws IllegalArgumentException if the entry is null
     */
    public void addEntry(Entry entry) throws IllegalArgumentException {
        if (entry == null) {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        entries.put(entry.getTitle(), entry);
    }

    /**
     * Retrieves an entry from the rulebook by its title.
     *
     * @param title the title of the entry to retrieve
     * @return the entry associated with the given title, or null if not found
     * @throws IllegalArgumentException if the title is null
     */
    public Entry getEntry(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        return entries.get(title);
    }

    /**
     * Retrieves all titles of entries in the rulebook.
     *
     * @return  a list of all entry titles
     */
    public List<String> getAllTitles() {
        return new ArrayList<>(entries.keySet());
    }

    /**
     * Retrieves the title of the rulebook.
     *
     * @return  the title of the rulebook
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the rulebook.
     *
     * @param title the new title to set
     * @throws IllegalArgumentException if the title is null
     */
    public void setTitle(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.title = title;
    }
}
