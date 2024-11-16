package cocoismagik.datastructures;

import java.util.HashMap;
import java.util.Map;

import cocoismagik.main.DataOutputter;

public class CharacterCreationThreadDetailsEmbedTracker {
    // Static map to store thread (key) to user (value) mappings
    private static final Map<Long, Long> threadToMessageMap = new HashMap<>();

    // Private constructor to prevent instantiation
    private CharacterCreationThreadDetailsEmbedTracker() {
    }

    
    
    /**
     * Associates a message with a thread ID, marking the message as the details embed
     * for the thread.
     * 
     * @param messageId the ID of the message to associate with the thread
     * @param threadId  the ID of the thread to associate with the message
     */
    public static void addMessage(long messageId, long threadId) {
        // Check if the thread is already owned by another user
        if (threadToMessageMap.containsKey(threadId)) {
            Long existingMessageId = threadToMessageMap.get(threadId);
            String message = "Thread ID " + threadId + " already has details embed of ID " + existingMessageId
                    + ", cannot assign to User ID " + messageId;
            DataOutputter.logMessage(message, DataOutputter.WARNING);
            return; // Exit the method without adding the ownership
        }

        // Associate the thread with the message
        threadToMessageMap.put(threadId, messageId);
    }

    
    
    /**
     * Removes the message associated with a thread ID as the details embed for that thread.
     * 
     * @param messageId the ID of the message to remove
     * @param threadId  the ID of the thread whose associated message is to be removed
     */
    public static void removeMessage(long messageId, long threadId) {
        // Check if the thread is owned by the user
        if (!threadToMessageMap.containsKey(threadId)) {
            String message = "Thread ID " + threadId + " does not have details embed of ID " + messageId
                    + ", cannot remove from User ID " + messageId;
            DataOutputter.logMessage(message, DataOutputter.WARNING);
            return; // Exit the method without removing ownership
        }

        // Remove the thread from the map
        threadToMessageMap.remove(threadId);
    }

    
    /**
     * Retrieves the message ID associated with a thread ID as the details embed for that thread.
     * 
     * @param threadId the ID of the thread to retrieve the message ID for
     * @return the message ID associated with the thread, or null if not found
     */
    public static Long getDetailsEmbedMessageLong(long threadId) {
        return threadToMessageMap.getOrDefault(threadId, null);
    }
}
