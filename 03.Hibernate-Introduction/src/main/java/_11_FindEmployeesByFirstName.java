import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _11_FindEmployeesByFirstName {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please, enter pattern: ");
        String pattern = scan.nextLine();

        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.firstName LIKE concat(:pattern, '%')", Employee.class)
                .setParameter("pattern", pattern)
                .getResultStream()
                .forEach(e -> {
                    System.out.printf("%s %s - %s - ($%.2f)%n", e.getFirstName(), e.getLastName(),
                            e.getJobTitle(), e.getSalary());
                });

        entityManager.close();
    }
}
