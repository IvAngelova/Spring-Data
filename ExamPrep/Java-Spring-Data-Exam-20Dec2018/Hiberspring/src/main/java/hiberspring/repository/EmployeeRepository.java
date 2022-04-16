package hiberspring.repository;

import hiberspring.domain.dtos.ProductiveEmployeeDto;
import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT new hiberspring.domain.dtos.ProductiveEmployeeDto(concat(e.firstName, ' ', e.lastName), e.position, e.card.number)" +
            "FROM Employee e " +
            "WHERE e.branch.products.size > 0 " +
            "ORDER BY concat(e.firstName, ' ', e.lastName) ASC, length(e.position) DESC")
    List<ProductiveEmployeeDto> findAllEmployeesWithBranchWithMinOneProduct();

    Employee findEmployeeByCard_Number(String card_number);

}
