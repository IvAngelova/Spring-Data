import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class _02_ChangeCasing {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

//        entityManager.getTransaction().begin();
//
//        entityManager.createQuery("UPDATE Town t" +
//               " SET t.name = upper(t.name) WHERE length(t.name) <= 5").executeUpdate();
//
//        entityManager.getTransaction().commit();

        List<Town> towns = entityManager
                .createQuery("SELECT t FROM Town t", Town.class)
                .getResultList();

        entityManager.getTransaction().begin();
        for (Town town : towns) {
            String townName = town.getName();
            if (townName.length() <= 5) {
                String toUpper = townName.toUpperCase();
                town.setName(toUpper);
                entityManager.persist(town);
            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();

    }
}
