import java.sql.*;
import java.util.Scanner;

public class _09_IncreaseAgeStoredProcedure {

    public static void main(String[] args) throws SQLException {
        Connection connection = MyConnection.getConnection();

        Scanner scan = new Scanner(System.in);
        int minionId = Integer.parseInt(scan.nextLine());

        CallableStatement callableStatement = connection.
                prepareCall("CALL usp_get_older(?);");

        callableStatement.setInt(1, minionId);

        callableStatement.executeUpdate();

        printMinionInfo(connection, minionId);

        connection.close();

    }

    private static void printMinionInfo(Connection connection, int minionId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name, age FROM minions WHERE id = ?;");

        preparedStatement.setInt(1, minionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String minionName = resultSet.getString("name");
        int minionAge = resultSet.getInt("age");
        System.out.printf("%s %d%n", minionName, minionAge);
    }
}
