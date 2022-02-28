import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _06_AddingNewAddressUpdatingEmployee {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please, enter employee last name: ");
        String lastName = scan.nextLine();

        String addressText = "Vitoshka 15";

        Address address = insertAddress(entityManager, addressText);

        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static Address insertAddress(EntityManager entityManager, String addressText) {
        entityManager.getTransaction().begin();
        Address address = new Address();
        address.setText(addressText);
        entityManager.persist(address);
        entityManager.getTransaction().commit();
        return address;
    }
}
