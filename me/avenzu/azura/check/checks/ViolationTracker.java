package me.avenzu.azura.check.checks;

import java.util.HashMap;
import java.util.UUID;

public class ViolationTracker {
    private static final HashMap<UUID, Integer> violations = new HashMap<>();

    public static void addViolation(UUID playerUUID) {
        violations.put(playerUUID, violations.getOrDefault(playerUUID, 0) + 1);
    }

    public static int getViolations(UUID playerUUID) {
        return violations.getOrDefault(playerUUID, 0);
    }

    public static void resetViolations(UUID playerUUID) {
        violations.remove(playerUUID);
    }
}
