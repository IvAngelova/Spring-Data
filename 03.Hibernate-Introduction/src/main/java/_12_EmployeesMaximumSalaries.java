import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@SuppressWarnings("unchecked")
public class _12_EmployeesMaximumSalaries {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        List<Object[]> rows = entityManager.createNativeQuery("SELECT d.name , max(e.salary) as max_salary " +
                        "from departments d " +
                        "join employees e on d.department_id = e.department_id " +
                        "group by d.name " +
                        "having max_salary NOT BETWEEN 30000 AND 70000;")
                .getResultList();

        rows.forEach(row -> {
            System.out.printf("%s - %.2f%n", row[0], Double.parseDouble(String.valueOf(row[1])));
        });

        entityManager.close();
    }
}
