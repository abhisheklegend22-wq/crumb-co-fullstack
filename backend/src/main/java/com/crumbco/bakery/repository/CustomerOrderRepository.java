package com.crumbco.bakery.repository;
import com.crumbco.bakery.model.CustomerOrder; import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {}
