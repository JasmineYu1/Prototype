import java.util.*;

public class Calculator {

    private static final double K1 = 0.5; // Scale factor for the upper threshold
    private static final double K2 = 0.5; // Scale factor for the lower threshold
    private static final int totalWeeks = 4; //hardcoding totalWeeks

    public Map<String, Map<String, Double>> calculateThresholds(
                Map<String, Map<String, List<Integer>>> occupancyMap,

    ) {
        Map<String, Map<String, Double>> thresholds = new HashMap<>();

        for (String dayHour : occupancyMap.keySet()) {
            Map<String, List<Integer>> presentMap = occupancyMap.get(dayHour);
            Map<String, Double> userThresholds = new HashMap<>();

            for (String user : userCounts.keySet()) {
                List<Integer> counts = userCounts.get(username);

                    // Calculate mean (A1 or A2)
                double mean = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);
                    //Calculate A1
                double mean1 = counts.stream().mapToDouble(Integer::doubleValue).sum() / totalWeeks;
                    //Calculate A2
                double mean2 = counts.stream().mapToDouble(Integer::doubleValue).sum() / totalWeeks;
                    //Calculate S1
                    //Calculate S2

                    // Calculate standard deviation (S1 or S2)
                double variance = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDev = Math.sqrt(variance);

                    // Calculate thresholds
                double upperThreshold = Math.min(1.0, mean + (K1 * stdDev));
                double lowerThreshold = Math.max(0.0, mean - (K2 * stdDev));

                    // Store thresholds
                userThresholds.put(username + "_upper", upperThreshold);
                userThresholds.put(username + "_lower", lowerThreshold);
            }

            thresholds.put(dayHour, hourThresholds);
        }

        return thresholds;
    }
}
