import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;


public class _04_EmployeesWithSalary {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.createQuery("SELECT e.firstName FROM Employee e " +
                        "WHERE e.salary > :min_salary", String.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000))
                .getResultList()
                .forEach(System.out::println);

        entityManager.close();
    }
}
