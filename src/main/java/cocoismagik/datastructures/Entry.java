package cocoismagik.datastructures;

public class Entry {

    private String title;
    private String category;
    private String markdownText;

    public Entry(String title, String category, String markdownText) throws IllegalArgumentException {
        if (title == null || category == null || markdownText == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
        this.title = title;
        this.category = category;
        this.markdownText = markdownText;
    }
    
    /**
     * Returns the title of the Entry object.
     *
     * @return The title of the Entry object.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the entry.
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

    /**
     * Returns the category of the Entry object.
     *
     * @return The category of the Entry object.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the Entry object.
     *
     * @param category the new category to set
     * @throws IllegalArgumentException if the vategory is null
     */
    public void setCategory(String category) throws IllegalArgumentException {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /**
     * Returns the markdown text of the Entry object.
     *
     * @return The markdown text of the Entry object.
     */
    public String getMarkdownText() {
        return markdownText;
    }

    /**
     * Sets the markdown text of the Entry object.
     *
     * @param markdownText the new markdown text to set
     * @throws IllegalArgumentException if the markdown text is null
     */
    public void setMarkdownText(String markdownText) throws IllegalArgumentException {
        if (markdownText == null) {
            throw new IllegalArgumentException("Markdown text cannot be null");
        }
        this.markdownText = markdownText;
    }
}
