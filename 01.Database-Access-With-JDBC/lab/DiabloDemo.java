package db_apps_intro.lab;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DiabloDemo {
    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter user: ");
        String username = scan.nextLine();
        System.out.println("Enter password: ");
        String password = scan.nextLine().trim();

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", props);

        PreparedStatement statement = connection.prepareStatement("SELECT u.user_name, u.first_name, " +
                "u.last_name, count(ug.game_id) AS 'count' " +
                "FROM users AS u " +
                "JOIN users_games AS ug " +
                "ON u.id = ug.user_id " +
                "WHERE user_name = ?;");

        String userName = scan.nextLine();
        statement.setString(1, userName);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            if (resultSet.getString(1) == null) {
                System.out.println("No such user exists");
            } else {
                System.out.printf("User: %s\n" +
                                "%s %s has played %d games\n", resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt("count"));
            }
        }

            connection.close();
        }
    }
