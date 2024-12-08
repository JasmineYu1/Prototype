import java.util.*;
import java.sql.*;

public class occupancyHashmap {

     public static final String USER = "occupants";//"YourUsername";
     public static final String PASS = "password";//"YourPassword";
     public static final String URL = "jdbc:postgresql://localhost:5432/occupancymapbase"; // name of database goes here


    public static Map<String, Map<String, List<Integer>>> occupantMap() {
       Map <String,Map<String,List<Integer>>> occupancyMap = new HashMap<>();
       String sql = "SELECT  * FROM occupancy"; // this is where we will put our string for the sql , will change sepending on how database looks like

       try( Connection connection = DriverManager.getConnection(URL,USER,PASS);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(); // add statement to get sql table and add on to here
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

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }



}
