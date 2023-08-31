package com.vgur.spring.core.repositories;

import com.vgur.spring.core.entities.Order;
import com.vgur.spring.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.userName = ?1")
    List<Order> findAllByUsername(String username);
}
