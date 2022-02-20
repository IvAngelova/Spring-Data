import java.sql.*;
import java.util.Scanner;

public class _03_GetMinionNames {

    public static void main(String[] args) throws SQLException {

        Connection connection = MyConnection.getConnection();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter villain id: ");
        int villainId = Integer.parseInt(scan.nextLine());

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT v.name " +
                        "FROM villains AS v " +
                        "WHERE v.id = ?;");

        preparedStatement.setInt(1, villainId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            return;
        }

        System.out.printf("Villain: %s%n", resultSet.getString(1));

        printMinionsByVillainId(connection, villainId);

        connection.close();

    }

    private static void printMinionsByVillainId(Connection connection, int villainId) throws SQLException {
        PreparedStatement prepStatMinions = connection.prepareStatement("SELECT m.name, m.age " +
                "FROM minions AS m " +
                "JOIN minions_villains mv on m.id = mv.minion_id " +
                "WHERE mv.villain_id = ?;");

        prepStatMinions.setInt(1, villainId);

        ResultSet minionsSet = prepStatMinions.executeQuery();

        int counter = 1;
        while (minionsSet.next()) {
            System.out.printf("%d. %s %d%n", counter++,
                    minionsSet.getString(1), minionsSet.getInt(2));
        }
    }

}
