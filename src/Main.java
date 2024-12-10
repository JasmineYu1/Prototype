//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Map;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Starting Occupancy-based task scheduling system!");

//        for (int i = 1; i <= 5; i++) {
//            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//            System.out.println("i = " + i);
//        }

        //code to declare new instance & perform calculations on data
        //calculate B1 and B2: B1 is equal to the mean of presence count for user A for each day_hour time slot
        //B2 is equivalent to that of B1, except for the fact that it is mean of user B
        //each time a value of B1 or B2 is calculated for a given time slot, the value is first compared to the upperThreshold of user A / B
        //if B1 / B2 > upperThreshold, print "User A / B can do housework at xx:xx on [weekday]!" and B1 / B2 is going to passed on
        //for further comparison with the lowerThreshold of user A / B
        //if B1 / B2 < lowerThreshold, print "User A / B can use shared facilities at xx:xx on [weekday]!"
        //if B1 / B2 < upperThreshold in the first place, print nothing and continue to check for the next value of B1 / B2


        // Create an instance of occupancyHashmap and fetch data
        occupancyHashmap hashmapGenerator = new occupancyHashmap();
        Map<String, Map<String, List<Integer>>> occupancyMap = hashmapGenerator.occupantMap();

        // Create an instance of Calculator and perform calculations
        OccupantMap occupantMap = new OccupantMap();
        Map<String, Map<String, Double>> thresholds = occupantMap.calculateThresholds();

                //calculateThresholds();

        // Analyze the data using the calculated thresholds
        for (String dayHour : occupancyMap.keySet()) {
            Map<String, List<Integer>> userCounts = occupancyMap.get(dayHour);
            Map<String, Double> userThresholds = thresholds.get(dayHour);

            for (String username : userCounts.keySet()) {
                List<Integer> counts = userCounts.get(username);

                double meanPresence = counts.stream().mapToDouble(Integer::doubleValue).sum() / (Calculator.totalWeeks * 7 * 14);

                // Compare meanPresence to thresholds
                double upperThreshold = userThresholds.get(username + "_upper");
                double lowerThreshold = userThresholds.get(username + "_lower");

                if (meanPresence > upperThreshold) {
                    System.out.printf("User %s can do housework at %s!\n", username, dayHour);
                } else if (meanPresence < lowerThreshold) {
                    System.out.printf("User %s can use shared facilities at %s!\n", username, dayHour);
                }
            }
        }
    }
}
