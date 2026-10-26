package com.example.demo;

import com.example.demo.OrderInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInquiryRepository extends JpaRepository<OrderInquiry, Long> {}