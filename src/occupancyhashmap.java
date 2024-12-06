import java.util.*;
import java.sql.*;

public class occupancyhashmap {

     public static final String USERNAME = "occupants";//"YourUsername";
     public static final String PASSWORD = "password";//"YourPassword";
     public static final String URL = "jdbc:postgresql://localhost:5432/occupancymapbase"; // name of database goes here


    public static void main(String[] args) {

    }

    public static Map<String, Map<String, List<Integer>>> occupantMap() {
       Map <String,Map<String,List<Integer>>> occupancyMap = new HashMap<>();
       String sql = "SELECT  * FROM occupancy"; // this is where we will put our string for the sql

       try( Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(); // add statement to get sql table and add on to here
       )
       {
           while(true){
               // nested hashmap gets data rom the sh  cloud - which will be our SQL so we just make a hashmap and then calculate it ans use calculations to get what they need to doo
               //outer layer - using each string combination of day of week and hour in 24 hours system as the key
               // inner layer - each users’ name will be used as keys to identify the presence counts for the particular individual from 8 a.m. to 22 p.m
               String day = resultSet.getString("day_id");
               String hour = resultSet.getString("hour_id"); // this will be the hour for the outer layer
               String dayHour = day + "_" + hour;
               String username = resultSet.getString("user_id");
               //present count as for the inner layer too
               int count = resultSet.getInt("count");

              occupancyMap.putIfAbsent(dayHour, new HashMap<>());
              Map<String, List<Integer>> presentMap = occupancyMap.get(dayHour); // inner loop
              presentMap.putIfAbsent(username,new ArrayList<>());
              presentMap.get(username).add(count);

              // presentMap.put(username, presentMap.get(username) + 1);
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
//Structure of database (in SSH Cloud):
//Columns: day_id, hour, user

// Structure of Hashmap:
//dayHour outer key, in the form of day_hour, specifying each hourly time slot each day in the week
//username inner key, same as user_id, specifying the