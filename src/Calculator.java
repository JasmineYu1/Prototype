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

            for (String user : userCounts.keySet()) { //need further clarifications
                List<Integer> counts = userCounts.get(username);

                    //Calculate A1
                //calculate the sum of presence counts for A and B, divided by total number of days)
                //meanA1 and meanB1 may be merged and replaced by a single mean1
                double meanA1 = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);
                double meanB1 = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);
                    //Calculate A2
                //Calculate the sum of all presence counts for both users, divided by total number of days x 2)
                double mean2 = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14 * 2);


                    // Calculate standard deviation
                //Calulate S1 for A and B, by subtracting meanA1 and meanB1 respectively from each count data point
                double varianceA1 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevA1 = Math.sqrt(variance);

                double varianceB1 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevB1 = Math.sqrt(variance);

                //Calculate S2 for A and B
                double varianceA2 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevA2 = Math.sqrt(variance);
                double varianceB2 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - mean, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevB2 = Math.sqrt(variance);


                    // Calculate thresholds
                double upperThresholdA = Math.min(1.0, meanA1 + (K1 * stdDevA1));
                double lowerThresholdA = Math.max(0.0, mean2 - (K2 * stdDevA2));

                double upperThresholdB = Math.min(1.0, meanB1 + (K2 * stdDevB1));
                double lowerThresholdB = Math.max(0.0, mean2 - (K2 * stdDevB2));

                    // Store thresholds
                userThresholds.put(username + "_upper", upperThreshold);
                userThresholds.put(username + "_lower", lowerThreshold);
            }

            thresholds.put(dayHour, hourThresholds);
        }

        return thresholds;
    }
}
