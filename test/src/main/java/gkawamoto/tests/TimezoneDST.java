package gkawamoto.tests;

import java.util.Calendar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class TimezoneDST {
    public static void main(String[] args) throws Exception {
        System.out.println("Horario do Java / Java time");
        System.out.println(Calendar.getInstance());
        Connection connection = connectDatabase();
        testCurrentTimestamp(connection);
        testInsert(connection);
    }
    private static void testInsert(Connection connection) throws Exception {
        PreparedStatement stmt = connection.prepareStatement("TRUNCATE timezone_dst_test");
        stmt.executeUpdate();
        stmt.close();
        stmt = connection.prepareStatement("INSERT INTO timezone_dst_test(date_with_time_zone, date_without_time_zone) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        stmt.executeUpdate();
        stmt.close();
        stmt = connection.prepareStatement("SELECT date_without_time_zone, date_with_time_zone FROM timezone_dst_test");
        ResultSet set = stmt.executeQuery();
        set.next();
        System.out.println("Timestamp com e sem time zone inseridas no banco / Timestamp with and without time zone inserted in the table");
        System.out.println(set.getTimestamp(1));
        System.out.println(set.getTimestamp(2));
        set.close();
        stmt.close();
    }
    private static void testCurrentTimestamp(Connection connection) throws Exception {
        System.out.println("Timestamp direto na select / Timestamp from the SELECT");
        PreparedStatement stmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP");
        ResultSet set = stmt.executeQuery();
        set.next();
        System.out.println(set.getTimestamp(1));
        set.close();
        stmt.close();
    }
    private static Connection connectDatabase() throws Exception {
        while (true) {
            String username = System.getenv().getOrDefault("POSTGRES_USER", "test");
            String password = System.getenv().getOrDefault("POSTGRES_PASSWORD", "test");
            String connectionString = System.getenv().getOrDefault("POSTGRES_CONNECTION_STRING", "jdbc:postgresql://database:5432/test");
            try {
                Connection connection = DriverManager.getConnection(connectionString, username, password);
                connection.setAutoCommit(true);
                return connection;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
    }
}
