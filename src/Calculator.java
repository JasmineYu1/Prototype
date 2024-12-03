import java.util.*;

public class Calculator {

    private static final double K1 = 0.5; // Scale factor for the upper threshold
    private static final double K2 = 0.5; // Scale factor for the lower threshold

    public Map<String, Map<String, Double>> calculateThresholds(
                Map<String, Map<String, List<Integer>>> detailedOccupancyMap,
                int totalWeeks
    ) {
        Map<String, Map<String, Double>> thresholds = new HashMap<>();

        for (String dayHour : detailedOccupancyMap.keySet()) {
            Map<String, List<Integer>> userCounts = detailedOccupancyMap.get(dayHour);
            Map<String, Double> hourThresholds = new HashMap<>();

            for (String user : userCounts.keySet()) {
                List<Integer> counts = userCounts.get(user);

                    // Calculate mean (A1 or A2)
                double mean = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);

                    // Calculate standard deviation (S1 or S2)
                double variance = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDev = Math.sqrt(variance);

                    // Calculate thresholds
                double upperThreshold = Math.min(1.0, mean + (K1 * stdDev));
                double lowerThreshold = Math.max(0.0, mean - (K2 * stdDev));

                    // Store thresholds
                hourThresholds.put(user + "_upper", upperThreshold);
                hourThresholds.put(user + "_lower", lowerThreshold);
            }

            thresholds.put(dayHour, hourThresholds);
        }

        return thresholds;
    }
}
