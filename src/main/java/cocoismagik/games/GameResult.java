package cocoismagik.games;

import java.util.List;
import java.util.Map;

public interface GameResult {
    String getActionDescription();
    boolean isSuccess();
    int getValue();
    Map<String, Object> getAddtionalData();
    List<String> getAffectedEntities();
}
