import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class _08_GetEmployeeWithProject {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.println("Please, enter employee id: ");
        Integer id = Integer.parseInt(scan.nextLine());

        Employee employee = entityManager.find(Employee.class, id);

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());

//        Set<String> projectsNames = new TreeSet<>();
//
//        for (Project project : employee.getProjects()) {
//            String projectName = project.getName();
//            projectsNames.add(projectName);
//        }
//
//        projectsNames.forEach(p -> System.out.printf("\t%s%n", p));

        employee.getProjects()
                .stream()
                .map(Project::getName)
                .sorted()
                .forEach(p -> System.out.printf("\t%s%n", p));

        entityManager.close();
    }
}
