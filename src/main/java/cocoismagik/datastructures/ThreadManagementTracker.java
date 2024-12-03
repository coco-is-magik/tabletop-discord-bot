package cocoismagik.datastructures;

import java.util.List;
import java.util.Map;
import cocoismagik.main.DataOutputter;

import java.util.ArrayList;
import java.util.HashMap;

public class ThreadManagementTracker {
    // Static map to manage thread ownership by users
    private static final Map<Long, List<Long>> userToThreadsMap = new HashMap<>();

    // Static map to manage thread-related mappings using type keys
    private static final Map<Long, Map<Integer, Long>> threadDataMap = new HashMap<>();

    // Constants to identify types of associated data
    public static final int DETAILS_EMBED = 1;
    public static final int BUTTONS_EMBED = 2;

    // Private constructor to prevent instantiation
    private ThreadManagementTracker() {
    }

    // Ownership Methods

    /**
     * Associates a user with a thread, marking the thread as owned by the user.
     * 
     * @param userId  the ID of the user to associate with the thread
     * @param threadId the ID of the thread to associate with the user
     * 
     * @throws IllegalArgumentException if the thread is already owned by another user
     */
    public static void addThreadOwnership(long userId, long threadId) throws IllegalArgumentException {
        DataOutputter.logMessage("Adding thread ownership for User ID " + userId + " and Thread ID " + threadId, DataOutputter.INFO);
        // Check if the thread is already owned by another user to avoid double ownership
        for (Map.Entry<Long, List<Long>> entry : userToThreadsMap.entrySet()) {
            Long existingUserId = entry.getKey();
            List<Long> threads = entry.getValue();

            if (threads.contains(threadId)) {
                String s = "Thread ID " + threadId + " is already owned by User ID " + existingUserId + ", cannot assign to User ID " + userId;
                // Throw an exception to indicate double ownership, so that the caller can handle it
                throw new IllegalArgumentException(s);
            }
        }

        // Associate the user with the thread as long as there is no double ownership
        userToThreadsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(threadId);
    }

    /**
     * Disassociates a user from a thread, marking the thread as no longer owned by the user.
     * 
     * @param userId   the ID of the user to disassociate from the thread
     * @param threadId the ID of the thread to disassociate from the user
     */
    public static void removeThreadOwnership(long userId, long threadId) {
        DataOutputter.logMessage("Removing thread ownership for User ID " + userId + " and Thread ID " + threadId, DataOutputter.INFO);
        List<Long> threads = userToThreadsMap.get(userId);
        if (threads != null) {
            threads.remove(threadId);
            if (threads.isEmpty()) {
                userToThreadsMap.remove(userId);
            }
        }
    }

    /**
     * Checks if a user owns a thread.
     * 
     * @param userId  the ID of the user to check
     * @param threadId the ID of the thread to check
     * @return true if the user owns the thread, false if not
     */
    public static boolean ownsThread(long userId, long threadId) {
        DataOutputter.logMessage("Checking if User ID " + userId + " owns Thread ID " + threadId, DataOutputter.INFO);
        List<Long> threads = userToThreadsMap.get(userId);
        return threads != null && threads.contains(threadId);
    }

    // Generic Data Methods

    /**
     * Associates data with a thread under a specific type key.
     *
     * @param threadId the ID of the thread
     * @param typeKey  the type of data (e.g., DETAILS_EMBED, BUTTONS_EMBED)
     * @param dataId   the ID of the data to associate with the thread
     */
    public static void addThreadData(long threadId, int typeKey, long dataId) {
        DataOutputter.logMessage("Adding data ID " + dataId + " to Thread ID " + threadId + " under type key " + typeKey, DataOutputter.INFO);
        threadDataMap.computeIfAbsent(threadId, k -> new HashMap<>()).put(typeKey, dataId);
    }

    /**
     * Removes the data associated with a thread under a specific type key.
     *
     * @param threadId the ID of the thread
     * @param typeKey  the type of data (e.g., DETAILS_EMBED, BUTTONS_EMBED)
     */
    public static void removeThreadData(long threadId, int typeKey) {
        DataOutputter.logMessage("Removing data from Thread ID " + threadId + " under type key " + typeKey, DataOutputter.INFO);
        Map<Integer, Long> typeMap = threadDataMap.get(threadId);
        if (typeMap != null) {
            typeMap.remove(typeKey);
            if (typeMap.isEmpty()) {
                threadDataMap.remove(threadId);
            }
        }
    }

    /**
     * Retrieves the data ID associated with a thread under a specific type key.
     *
     * @param threadId the ID of the thread
     * @param typeKey  the type of data (e.g., DETAILS_EMBED, BUTTONS_EMBED)
     * @return the ID of the data, or null if not found
     */
    public static Long getThreadData(long threadId, int typeKey) {
        DataOutputter.logMessage("Retrieving data from Thread ID " + threadId + " under type key " + typeKey, DataOutputter.INFO);
        Map<Integer, Long> typeMap = threadDataMap.get(threadId);
        if (typeMap == null) {
            DataOutputter.logMessage("No data found for Thread ID " + threadId + " under type key " + typeKey, DataOutputter.WARNING);
        }
        return typeMap != null ? typeMap.get(typeKey) : null;
    }
}

