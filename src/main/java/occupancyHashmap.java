
/* code has been moved to class Occupant map as we decided it would be better to combine both the hashmap
 and calculations method so the hashmap is easily accessible*/

import java.util.*;
import java.sql.*;

public class occupancyHashmap {

     public static final String USERNAME = "postgres";
     public static final String PASSWORD = "Lime8629!";
     public static final String URL = "jdbc:postgresql://localhost:5433/ssh_database";
      public static Map <String,Map<String,List<Integer>>> occupancyMap;


    public static Map<String, Map<String, List<Integer>>> occupantMap() {

        String sql = "SELECT  * FROM occupancy";

       try( Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
       )

       {
           while(true){

               String day,hour,date,username;
               day = resultSet.getString("day_id");
               hour = resultSet.getString("hour_id");
               date = day + "-" + hour;
               username = resultSet.getString("user_id");

               int count = resultSet.getInt("count");

              occupancyMap.putIfAbsent(date, new HashMap<>());
              Map<String, List<Integer>> presentMap = occupancyMap.get(date); // inner loop
              presentMap.putIfAbsent(username,new ArrayList<>());
              presentMap.get(username).add(count);

               connection.close();



                return occupancyMap;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}

