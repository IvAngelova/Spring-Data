import java.sql.*;
import java.util.Scanner;

public class _04_AddMinion {

    public static void main(String[] args) throws SQLException {

        Connection connection = MyConnection.getConnection();

        Scanner scan = new Scanner(System.in);
        String[] minionInfo = scan.nextLine().split("\\s+");
        String villainName = scan.nextLine().split("\\s+")[1];

        String minionName = minionInfo[1];
        int minionAge = Integer.parseInt(minionInfo[2]);
        String minionTown = minionInfo[3];

        int townId = getEntityId(connection, minionTown, "towns");

        if (townId == 0) {
            addMinionTownToDB(connection, minionTown);
            System.out.printf("Town %s was added to the database.%n", minionTown);
            townId = getEntityId(connection, minionTown, "towns");
        }

        int villainId = getEntityId(connection, villainName, "villains");
        if (villainId == 0) {
            addVillainToDB(connection, villainName);
            System.out.printf("Villain %s was added to the database.%n", villainName);
            villainId = getEntityId(connection, villainName, "villains");
        }

        addMinionToDB(connection, minionName, minionAge, townId);

        int minionId = getEntityId(connection, minionName, "minions");

        insertMinionsVillains(connection, villainId, minionId);
        System.out.printf("Successfully added %s to be minion of %s.", minionName, villainName);

        connection.close();

    }

    private static void insertMinionsVillains(Connection connection, int villainId, int minionId) throws SQLException {
        PreparedStatement insertMinionsVillains = connection.
                prepareStatement("INSERT INTO minions_villains VALUES (?, ?);");

        insertMinionsVillains.setInt(1, minionId);
        insertMinionsVillains.setInt(2, villainId);

        insertMinionsVillains.executeUpdate();
    }

    private static void addVillainToDB(Connection connection, String villainName) throws SQLException {
        PreparedStatement addVillain = connection
                .prepareStatement("INSERT into villains (name, evilness_factor) VALUES (?,?);");
        addVillain.setString(1, villainName);
        addVillain.setString(2, "evil");
        addVillain.executeUpdate();
    }

    private static void addMinionToDB(Connection connection, String minionName, int minionAge, int minionTownId) throws SQLException {
        PreparedStatement addMinion = connection
                .prepareStatement("INSERT into minions (name, age, town_id) VALUES (?,?,?);");
        addMinion.setString(1, minionName);
        addMinion.setInt(2, minionAge);
        addMinion.setInt(3, minionTownId);
        addMinion.executeUpdate();
    }

    private static void addMinionTownToDB(Connection connection, String minionTown) throws SQLException {
        PreparedStatement addTownToDB = connection
                .prepareStatement("INSERT INTO towns (name) VALUES (?);");
        addTownToDB.setString(1, minionTown);
        addTownToDB.executeUpdate();
    }

    private static int getEntityId(Connection connection, String entityName, String tableName) throws SQLException {
        String query = String.format("SELECT id FROM %s WHERE name = ?;", tableName);
        PreparedStatement getEntityId = connection.prepareStatement(query);
        getEntityId.setString(1, entityName);
        ResultSet resultSet = getEntityId.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }
}
