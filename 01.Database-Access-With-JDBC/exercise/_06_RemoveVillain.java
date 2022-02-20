import java.sql.*;
import java.util.Scanner;

public class _06_RemoveVillain {

    public static void main(String[] args) throws SQLException {
        Connection connection = MyConnection.getConnection();

        Scanner scan = new Scanner(System.in);
        int villainId = Integer.parseInt(scan.nextLine());

        String villainName = getVillainNameById(connection, villainId);
        if (villainName == null) {
            System.out.println("No such villain was found");
            return;
        }

        int releasedMinionsCount = releaseMinions(connection, villainId);

        deleteVillainById(connection, villainId);
        System.out.println(villainName + " was deleted");
        System.out.println(releasedMinionsCount + " minions released");

        connection.close();

    }

    private static void deleteVillainById(Connection connection, int villainId) throws SQLException {
        PreparedStatement deleteVillain = connection
                .prepareStatement("DELETE FROM villains " +
                        "WHERE id = ?;");
        deleteVillain.setInt(1, villainId);
        deleteVillain.executeUpdate();
    }

    private static int releaseMinions(Connection connection, int villainId) throws SQLException {
        PreparedStatement releaseMinions = connection
                .prepareStatement("DELETE FROM minions_villains " +
                        "WHERE villain_id = ?;");
        releaseMinions.setInt(1, villainId);
        return releaseMinions.executeUpdate();
    }

    private static String getVillainNameById(Connection connection, int villainId) throws SQLException {
        PreparedStatement getVillainName = connection.prepareStatement("SELECT name " +
                "FROM villains " +
                "WHERE id = ?;");
        getVillainName.setInt(1, villainId);
        ResultSet resultSet = getVillainName.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("name");
        }

        return null;
    }
}
