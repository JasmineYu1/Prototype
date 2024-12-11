import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class OccupantMapTest {

    @Test
    void testOccupantMap() {
        // Create a simplified test scenario with mock data
        OccupantMap.USERNAME = "postgres"; // Adjust username for test DB if needed
        OccupantMap.PASSWORD = "Lime8629!";
        OccupantMap.URL = "jdbc:postgresql://localhost:5432/residents"; // Update to point to a test database

        // Run the method
        Map<String, Map<String, List<Integer>>> result = OccupantMap.occupantMap();

        // Perform basic checks
        assertNotNull(result, "Result should not be null");
        assertTrue(result.containsKey("mon_21-22"), "Map should contain data for Mon-8");
        assertTrue(result.get("mon_21-22").containsKey("B"), "User 'A' should have data for Mon-8");
        assertEquals(1, result.get("mon_21-22").get("B").size(), "User 'A' should have one entry for Mon-8");
    }

    @Test
    void testCalculateThresholds() {
        // Mock the input for occupancyMap
        Map<String, Map<String, List<Integer>>> mockMap = new HashMap<>();
        Map<String, List<Integer>> userCounts = new HashMap<>();
        userCounts.put("A", Arrays.asList(1, 2, 3));
        userCounts.put("B", Arrays.asList(4, 5, 6));
        mockMap.put("mon_8-9", userCounts);

        // Inject mock data
        OccupantMap.occupancyMap = mockMap;

        // Run the method
        Map<String, Map<String, Double>> thresholds = OccupantMap.calculateThresholds();

        // Perform basic checks
        assertNotNull(thresholds, "Thresholds should not be null");
        assertTrue(thresholds.containsKey("mon_8-9"), "Thresholds should contain data for mon_8-9");
        assertTrue(thresholds.get("mon_8-9").containsKey("A_upper"), "Thresholds should include upper threshold for user 'A'");
        assertTrue(thresholds.get("mon_8-9").containsKey("A_lower"), "Thresholds should include lower threshold for user 'A'");
    }
}

