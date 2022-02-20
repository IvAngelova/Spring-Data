import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class _05_ChangeTownNamesCasing {

    public static void main(String[] args) throws SQLException {
        Connection connection = MyConnection.getConnection();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter country name: ");
        String countryName = scan.nextLine();

        PreparedStatement updateTownNames = connection.prepareStatement("UPDATE towns " +
                "SET name = UPPER(name) " +
                "WHERE country = ?;");

        updateTownNames.setString(1, countryName);
        int affectedRows = updateTownNames.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.println(affectedRows + " town names were affected.");

        List<String> towns = getTownNames(connection, countryName);

        System.out.println(towns);

        connection.close();

    }

    private static List<String> getTownNames(Connection connection, String countryName) throws SQLException {
        PreparedStatement preparedStatementTowns = connection.prepareStatement("SELECT name " +
                "FROM towns " +
                "WHERE country = ?;");

        preparedStatementTowns.setString(1, countryName);
        ResultSet resultSet = preparedStatementTowns.executeQuery();
        List<String> towns = new ArrayList<>();
        while (resultSet.next()) {
            towns.add(resultSet.getString("name"));
        }
        return towns;
    }

}
