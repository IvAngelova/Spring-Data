import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class _05_EmployeesFromDepartment {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :dep_name " +
                        "ORDER BY e.salary ASC, e.id ASC", Employee.class)
                .setParameter("dep_name", "Research and Development")
                .getResultStream()
                .forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f%n", employee.getFirstName(),
                            employee.getLastName(), employee.getDepartment().getName(), employee.getSalary());
                });

        entityManager.close();

    }
}
