import java.sql.*;
import java.util.*;

public class OccupantMap {

    public static String USERNAME = "postgres";
    public static String PASSWORD = "Lime8629!";
    public static String URL = "jdbc:postgresql://localhost:5432/residents";
   // public static String URL = "jdbc:postgresql://localhost:5432/ssh_database_schema.sql";
    public static Map <String,Map<String,List<Integer>>> occupancyMap;
    private static final double K1 = 0.5;
    private static final double K2 = 0.5;
    public static final int totalWeeks = 4;


    public static  Map<String, Map<String, List<Integer>>> occupantMap() {
        String sql = "SELECT  *  FROM  resident_presence ";

        try(
            Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)
        )

        {
            while(resultSet.next()){


                String day,hour,date,username;
                day = resultSet.getString("day");
                hour = resultSet.getString("hour");
                date = day + "_" + hour;
                username = resultSet.getString("user_id");
                int count = resultSet.getInt("count");

                occupancyMap = new HashMap<>();
                Map<String, List<Integer>> presentMap = new HashMap<>();
                presentMap.putIfAbsent(username,new ArrayList<>());
                presentMap.get(username).add(count);
                occupancyMap.putIfAbsent(date,presentMap);
                connection.close();

            }
            if (occupancyMap.isEmpty()) {
                System.out.println("No data fetched from the database.");
            }

        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }

        return occupancyMap;
    }


   public static Map<String, Map<String, Double>> calculateThresholds() {
        Map<String, Map<String, Double>> thresholds = new HashMap<>();

        for (String dayHour : occupancyMap.keySet()) {
            Map<String, List<Integer>> presentMap = occupancyMap.get(dayHour);
            Map<String, Double> userThresholds = new HashMap<>();

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



            for (String username : presentMap.keySet()) {

                List<Integer> counts = presentMap.get(username);

                double meanA1 = counts.stream().mapToDouble(Integer::doubleValue).sum() / (totalWeeks * 7 * 14);
                double varianceA1 = counts.stream()
                        .mapToDouble(count -> Math.pow(count - meanA1, 2))
                        .sum() / (totalWeeks * 7 * 14);
                double stdDevA1 = Math.sqrt(varianceA1);


                double upperThreshold = Math.min(1.0, meanA1 + (K1 * stdDevA1));
                double lowerThreshold = Math.max(0.0, meanA2 - (K2 * stdDevA2));

                userThresholds.put(username + "_upper", upperThreshold);
                userThresholds.put(username + "_lower", lowerThreshold);
            }

            thresholds.put(dayHour, userThresholds);
        }

        return thresholds;
    }

}

