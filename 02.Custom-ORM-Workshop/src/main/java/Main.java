import entities.User;
import orm.EntityManager;
import orm.MyConnector;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        MyConnector.createConnection("root", "", "mini-orm");
        Connection connection = MyConnector.getConnection();

        EntityManager<User> userEntityManager = new EntityManager<>(connection);

        // userEntityManager.doCreate(User.class);

        User user = new User("Ivan", 45, LocalDate.now());
        userEntityManager.persist(user);
        User user1 = new User("Trayana", 26, LocalDate.now());
        userEntityManager.persist(user1);
        Iterable<User> users = userEntityManager.find(User.class);
        System.out.println(users);

        User first = userEntityManager.findFirst(User.class, "id = 1");
        System.out.println(first);
        first.setUsername("Ivan_2");
        userEntityManager.persist(first);// doUpdate
        System.out.println(first);

        userEntityManager.delete(first);

    }
}
