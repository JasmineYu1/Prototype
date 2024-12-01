import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.util.*;
import java.sql.*;

public class occupancyhashmap {

     public static final String USERNAME = "occupants";//"YourUsername";
     public static final String PASSWORD = "password";//"YourPassword";
     public static final String URL = "jdbc:postgresql://localhost:5432/occupancymapbase"; // name of database goes here


    public static void main(String[] args) {

    }

    public static Map<String, Map<String, Integer>> hash() {
       Map <String,Map<String,Integer>> ocuppancyMap = new HashMap<>();
       String sql = " "; // this is where we will put our string for the sql

       try( Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql); // add statement to get sql table and add on to here
       )
       {
           while(true){
               // nested hashmap gets data rom the sh  cloud - which will be our SQL so we just make a hashmap and then calculate it ans use calculations to get what they need to doo
               //outer layer - using each string combination of day of week and hour in 24 hours system as the key
               // inner layer - each users’ name will be used as keys to identify the presence counts for the particular individual from 8 a.m. to 22 p.m
               String hour = resultSet.getString("time"); // this will be the hour for the outer layer

               //username as key for the inner layer
               String username = resultSet.getString("user_id");
               System.out.println(username);
               //present count as for the inner layer too
               int count = resultSet.getInt("count");

              ocuppancyMap.putIfAbsent(hour, new HashMap<>());
              Map<String, Integer> presentMap = ocuppancyMap.get(hour);
              presentMap.putIfAbsent(username, 0);
              presentMap.put(username, presentMap.get(username)+1);

               //closing connection after we have gotten what we have collected our data
               RowSetFactory factory = RowSetProvider.newFactory();  // adding rowset for jdbc
               CachedRowSet rowSet = factory.createCachedRowSet();
               rowSet.populate(resultSet);

               connection.close();
               statement.close();
               resultSet.close();
               return ocuppancyMap;

           }


       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }



}
