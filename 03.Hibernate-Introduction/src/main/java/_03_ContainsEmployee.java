
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _03_ContainsEmployee {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please, enter employee name:");
        String[] fullName = scan.nextLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];

        long count = entityManager.createQuery("SELECT count(e) FROM Employee e " +
                        "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        if (count == 0) {
            System.out.println("No");
        } else {
            System.out.println("Yes");
        }

        entityManager.close();

    }
}
