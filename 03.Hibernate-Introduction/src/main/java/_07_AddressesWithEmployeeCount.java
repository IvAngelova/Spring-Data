import entities.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class _07_AddressesWithEmployeeCount {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.createQuery("SELECT a FROM Address a " +
                        "ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(address -> {
                    System.out.printf("%s, %s - %d employees%n", address.getText(), address.getTown().getName(),
                            address.getEmployees().size());
                });

        entityManager.close();

    }
}
