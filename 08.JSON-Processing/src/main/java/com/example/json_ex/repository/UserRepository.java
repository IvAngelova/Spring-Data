package com.example.json_ex.repository;

import com.example.json_ex.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("select distinct u from User as u " +
//            "join u.soldProducts as sp " +
//            "where sp.buyer.id is not null " +
//            "order by u.lastName, u.firstName")
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE (SELECT COUNT (p) FROM Product p WHERE p.seller.id = u.id AND p.buyer.id IS NOT NULL) > 0 " +
            "ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersWithAtLeastOneItemSoldWithBuyerOrderByLastNameFirstName();


    @Query("SELECT u " +
            "FROM User u " +
            "WHERE size(u.soldProducts) > 0 " +
            "ORDER BY size(u.soldProducts) DESC, " +
            "u.lastName ASC ")
    List<User> findAllUsersWithMoreThanOneSoldProductOrderBySoldProductsDescLastNameAsc();
}
