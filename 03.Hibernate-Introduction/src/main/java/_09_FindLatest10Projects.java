
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class _09_FindLatest10Projects {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p " +
                        "ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(project -> {
                    System.out.printf("Project name: %s\n" +
                                    " \tProject Description: %s\n" +
                                    " \tProject Start Date:%s\n" +
                                    " \tProject End Date: %s\n",
                            project.getName(), project.getDescription(),
                            project.getStartDate().minusHours(3).format(formatter),
                            project.getEndDate());
                });

        entityManager.close();
    }
}
