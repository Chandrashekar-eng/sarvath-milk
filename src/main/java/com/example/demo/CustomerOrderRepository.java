package com.example.demo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {
    List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(String customerId);
    Optional<CustomerOrder> findByOrderId(String orderId);
}
