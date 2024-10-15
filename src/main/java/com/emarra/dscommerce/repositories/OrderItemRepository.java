package com.emarra.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emarra.dscommerce.entities.OrderItem;
import com.emarra.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}
