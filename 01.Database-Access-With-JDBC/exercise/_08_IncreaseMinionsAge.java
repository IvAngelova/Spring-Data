import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class _08_IncreaseMinionsAge {

    public static void main(String[] args) throws SQLException {

        Connection connection = MyConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE minions " +
                "SET age = age + 1, name = lower(name) " +
                "where id = ?;");

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter minion IDs: ");
        List<Integer> minionIds = Arrays.stream(scan.nextLine().split("\\s+")).map(Integer::parseInt)
                .collect(Collectors.toList());

        for (int i = 0; i < minionIds.size(); i++) {
            preparedStatement.setInt(1, minionIds.get(i));
            preparedStatement.executeUpdate();
        }

        printAllMinions(connection);

        connection.close();

    }

    private static void printAllMinions(Connection connection) throws SQLException {
        PreparedStatement allMinionsInfo = connection.prepareStatement("SELECT name, age FROM minions");

        ResultSet allMinionsRS = allMinionsInfo.executeQuery();

        while (allMinionsRS.next()) {
            System.out.println(allMinionsRS.getString("name") + " " + allMinionsRS.getString("age"));
        }
    }
}
