import java.sql.*;
import java.util.*;

public class _07_PrintAllMinionNames {

    public static void main(String[] args) throws SQLException {
        Connection connection = MyConnection.getConnection();

        PreparedStatement getMinionNames = connection.prepareStatement("SELECT name FROM minions");

        ResultSet resultSet = getMinionNames.executeQuery();

        List<String> minions = new ArrayList<>();

        while (resultSet.next()) {
            minions.add(resultSet.getString("name"));
        }

        for (int i = 0; i < minions.size() / 2; i++) {
            System.out.println(minions.get(i));
            System.out.println(minions.get(minions.size() - 1 - i));
        }

        if (minions.size() % 2 != 0) {
            System.out.println(minions.get(minions.size() / 2));
        }

        connection.close();

    }
}
