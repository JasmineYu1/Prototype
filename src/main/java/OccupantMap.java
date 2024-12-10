import java.sql.*;
import java.util.*;



public class OccupantMap {

    public static final String USERNAME = "postgres";//"YourUsername";
    public static final String PASSWORD = "Lime8629!";//"YourPassword";
    public static final String URL = "jdbc:postgresql://localhost:5433/ssh_database";
    public static Map <String,Map<String,List<Integer>>> occupancyMap;
    private static final double K1 = 0.5; // Scale factor for the upper threshold
    private static final double K2 = 0.5; // Scale factor for the lower threshold
    public static final int totalWeeks = 4; //hardcoding totalWeeks

    public static Map<String, Map<String, List<Integer>>> occupantMap() {
        //Map <String,Map<String,List<Integer>>> occupancyMap = new HashMap<>();
        String sql = "SELECT  * FROM resident_presence"; // this is where we will put our string for the sql , will change sepending on how database looks like

        try(
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql) // add statement to get sql table and add on to here
        )

        {
            while(true){
                // nested hashmap gets data rom the sh  cloud - which will be our SQL so we just make a hashmap and then calculate it ans use calculations to get what they need to doo
                //outer layer - using each string combination of day of week and hour in 24 hours system as the key
                // inner layer - each users’ name will be used as keys to identify the presence counts for the particular individual from 8 a.m. to 22 p.m

                String day,hour,date,username;
                day = resultSet.getString("day_id");
                hour = resultSet.getString("hour_id"); // this will be the hour for the outer layer
                date = day + "-" + hour;
                username = resultSet.getString("user_id");
                //present count as for the inner layer too
                int count = resultSet.getInt("count");

                occupancyMap.putIfAbsent(date, new HashMap<>()); // adds a key pair value to the map if the key is not there
                Map<String, List<Integer>> presentMap = occupancyMap.get(date); // inner loop
                presentMap.putIfAbsent(username,new ArrayList<>());
                presentMap.get(username).add(count);
                // closing connection after we have gotten what we have collected our data

                connection.close();
                statement.close();
                resultSet.close();


                return occupancyMap;
            }

        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }

    }


    public static Map<String, Map<String, Double>> calculateThresholds() {
        Map<String, Map<String, Double>> thresholds = new HashMap<>();
        occupancyHashmap occupancyHashmap = new occupancyHashmap();
        Map <String,Map<String,List<Integer>>> occupancyMap = occupancyHashmap.occupancyMap;

        for (String dayHour : occupancyMap.keySet()) {
            Map<String, List<Integer>> presentMap = occupancyMap.get(dayHour);
            Map<String, Double> userThresholds = new HashMap<>();

            // Calculate A2 and S2 (values for all users combined)
            // meanA2: sum of all presence counts for both users, divided by total number of days x 2
            //stdDevA2: subtract meanA2 from each data points of all users, square them and sum it, before dividing by 2 * totalWeeks *7 * 14 and square rooting everything
            double totalSum = 0;
            int totalCount = 0;
            for (List<Integer> counts : presentMap.values()) {
                totalSum += counts.stream().mapToDouble(Integer::doubleValue).sum();
                totalCount += counts.size();
            }
            double meanA2 = totalSum / (totalWeeks * 7 * 14 * 2);
            double varianceA2 = 0;
            for (List<Integer> counts : presentMap.values()) {
                varianceA2 += counts.stream()
                        .mapToDouble(count -> Math.pow(count - meanA2, 2))
                        .sum();
            }
            varianceA2 /= (totalWeeks * 7 * 14 * 2);
            double stdDevA2 = Math.sqrt(varianceA2);


            // Calculate thresholds for each user
            for (String username : presentMap.keySet()) {

                List<Integer> counts = presentMap.get(username);

                // Calculate A1 and S1 (values for the specific user)
                //meanA1: sum of presence counts for A and B respectively, divided by total number of days
                //stdDevA1: subtract meanA1 from each individual data points of user A and B only, square them and calculate sum, before dividing by totalWeeks * 7 * 14 and square rooting everything
                double meanA1 = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);
                double varianceA1 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - meanA1, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevA1 = Math.sqrt(varianceA1);


                // Calculate thresholds
                double upperThreshold = Math.min(1.0, meanA1 + (K1 * stdDevA1));
                double lowerThreshold = Math.max(0.0, meanA2 - (K2 * stdDevA2));

                // Store thresholds
                userThresholds.put(username + "_upper", upperThreshold);
                userThresholds.put(username + "_lower", lowerThreshold);
            }

            thresholds.put(dayHour, userThresholds); //further checks
        }

        return thresholds;
    }

}

// trying to get count from my hashmap
//count values
