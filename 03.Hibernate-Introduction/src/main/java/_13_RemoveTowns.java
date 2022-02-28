import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class _13_RemoveTowns {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please, enter town name: ");
        String townName = scan.nextLine();

        Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name = :name", Town.class)
                .setParameter("name", townName).getSingleResult();

        entityManager.getTransaction().begin();

        setEmployeeAddresses(town.getId(), entityManager);

        int affectedRows = deleteAddressesByTownName(townName, entityManager);

        entityManager.remove(town);

        System.out.printf("%d address%s in %s deleted%n", affectedRows,
                affectedRows == 1 ? "" : "es", townName);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static int deleteAddressesByTownName(String townName, EntityManager entityManager) {
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                        "WHERE a.town.name = :t_name", Address.class)
                .setParameter("t_name", townName)
                .getResultList();

        for (Address address : addresses) {
            entityManager.remove(address);
        }

        return addresses.size();
    }

    private static void setEmployeeAddresses(Integer townId, EntityManager entityManager) {
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.address.town.id = :t_id", Employee.class)
                .setParameter("t_id", townId)
                .getResultList();
        for (Employee employee : employees) {
            employee.setAddress(null);
            entityManager.persist(employee);
        }

    }
}
