package com.example.xml_ex.repository;

import com.example.xml_ex.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "WHERE (SELECT COUNT(p) FROM Product p WHERE p.seller.id = u.id AND p.buyer is not null) > 0 " +
            "ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersWithOneOrMoreSoldProductsOrderByLastNameThenFirstName();
}
