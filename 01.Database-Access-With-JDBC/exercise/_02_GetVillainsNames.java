import java.sql.*;

public class _02_GetVillainsNames {

    public static void main(String[] args) throws SQLException {

        Connection connection = MyConnection.getConnection();

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count_minions' " +
                        "FROM villains AS v " +
                        "JOIN minions_villains mv on v.id = mv.villain_id " +
                        "GROUP BY v.name " +
                        "HAVING count_minions > 15 " +
                        "ORDER BY count_minions DESC;");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            System.out.printf("%s %d%n", resultSet.getString(1), resultSet.getInt(2));
        }

        connection.close();

    }
}
