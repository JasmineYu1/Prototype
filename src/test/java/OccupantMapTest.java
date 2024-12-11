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


//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class OccupantMapTest {
//
//    OccupantMap occupantMap;
//
//    @BeforeEach
//    void setUp() {
//        occupantMap = new OccupantMap();
//    }
//
//    @Test
//    void testOccupantMapWithMockedDatabase() throws Exception {
//        // Mock database objects
//        Connection mockConnection = mock(Connection.class);
//        PreparedStatement mockStatement = mock(PreparedStatement.class);
//        ResultSet mockResultSet = mock(ResultSet.class);
//
//        // Mock the behavior of ResultSet
//        when(mockResultSet.next()).thenReturn(true, true, false); // Two rows, then end
//        when(mockResultSet.getString("day_id")).thenReturn("Mon");
//        when(mockResultSet.getString("hour_id")).thenReturn("8-9");
//        when(mockResultSet.getString("user_id")).thenReturn("A");
//        when(mockResultSet.getInt("count")).thenReturn(3);
//
//        // Mock the behavior of PreparedStatement and Connection
//        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
//
//        // Inject mock into DriverManager
//        mockStatic(DriverManager.class);
//        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);
//
//        // Call the method
//        Map<String, Map<String, List<Integer>>> result = occupantMap.occupantMap();
//
//        // Verify results
//        assertNotNull(result);
//        assertTrue(result.containsKey("Mon-8-9"));
//        assertEquals(1, result.get("Mon-8-9").size());
//        assertEquals(3, result.get("Mon-8-9").get("A").get(0));
//
//        // Verify mocks were called
//        verify(mockResultSet, times(2)).next();
//        verify(mockResultSet).getString("day_id");
//        verify(mockResultSet).getString("hour_id");
//        verify(mockResultSet).getString("user_id");
//        verify(mockResultSet).getInt("count");
//    }
//
//    @Test
//    void testCalculateThresholds() {
//        // Prepare mock data
//        Map<String, Map<String, List<Integer>>> mockOccupancyMap = new HashMap<>();
//        Map<String, List<Integer>> userPresence = new HashMap<>();
//        userPresence.put("A", Arrays.asList(2, 3, 4));
//        userPresence.put("B", Arrays.asList(1, 2, 3));
//        mockOccupancyMap.put("Mon-8-9", userPresence);
//
//        // Inject the mock map
//        OccupantMap.occupancyMap = mockOccupancyMap;
//
//        // Call the method
//        Map<String, Map<String, Double>> thresholds = occupantMap.calculateThresholds();
//
//        // Verify thresholds
//        assertNotNull(thresholds);
//        assertTrue(thresholds.containsKey("Mon-8-9"));
//
//        Map<String, Double> userThresholds = thresholds.get("Mon-8-9");
//        assertTrue(userThresholds.containsKey("A_upper"));
//        assertTrue(userThresholds.containsKey("A_lower"));
//        assertTrue(userThresholds.containsKey("B_upper"));
//        assertTrue(userThresholds.containsKey("B_lower"));
//
//        // Check that the values are within expected ranges
//        assertTrue(userThresholds.get("A_upper") > 0);
//        assertTrue(userThresholds.get("A_lower") >= 0);
//    }
//}
