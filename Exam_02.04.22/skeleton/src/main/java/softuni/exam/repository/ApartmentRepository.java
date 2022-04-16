package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

    boolean existsByAreaAndTown_TownName(Double area, String townName);
}
