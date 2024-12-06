import java.util.*;
import java.sql.*;

public class occupancyHashmap {

     public static final String USERNAME = "occupants";//"YourUsername";
     public static final String PASSWORD = "password";//"YourPassword";
     public static final String URL = "jdbc:postgresql://localhost:5432/occupancymapbase"; // name of database goes here

    public static Map<String, Map<String, List<Integer>>> occupantMap() {
       Map <String,Map<String,List<Integer>>> occupancyMap = new HashMap<>();
       String sql = "SELECT  * FROM occupancy"; // this is where we will put our string for the sql , will change sepending on how database looks like

       try( Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(); // add statement to get sql table and add on to here
       )

       {
           while(true){
               // nested hashmap gets data rom the sh  cloud - which will be our SQL so we just make a hashmap and then calculate it ans use calculations to get what they need to doo
               //outer layer - using each string combination of day of week and hour in 24 hours system as the key
               // inner layer - each users’ name will be used as keys to identify the presence counts for the particular individual from 8 a.m. to 22 p.m

               String day,hour,dayHour,username;
               day = resultSet.getString("day_id");
               hour = resultSet.getString("hour_id"); // this will be the hour for the outer layer
               dayHour = day + "-" + hour;
               username = resultSet.getString("user_id");

               //present count as for the inner layer too
               int count = resultSet.getInt("count");

              occupancyMap.putIfAbsent(dayHour, new HashMap<>()); // adds a key pair value to the map if the key is not there
              Map<String, List<Integer>> presentMap = occupancyMap.get(dayHour); // inner loop
              presentMap.putIfAbsent(username,new ArrayList<>());
              presentMap.get(username).add(count);

              // presentMap.put(username, presentMap.get(username) + 1);
               // closing connection after we have gotten what we have collected our data

//lll
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

// 1. change from storing integers of presence counts to storing a list of presence data
// 2. having day and hour ids separately, then linking day_id and hour_id using '_',
// naming it as the outer layer of hashmap
// 3. update this part accordingly:
//              ocuppancyMap.putIfAbsent(hour, new HashMap<>());
//              Map<String, Integer> presentMap = ocuppancyMap.get(hour);
//              presentMap.putIfAbsent(username, 0);
//              presentMap.put(username, presentMap.get(username)+1);
//
//               //closing connection after we have gotten what we have collected our data
//               RowSetFactory factory = RowSetProvider.newFactory();  // adding rowset for jdbc
//               CachedRowSet rowSet = factory.createCachedRowSet();
//               rowSet.populate(resultSet);