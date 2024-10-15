package com.emarra.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emarra.dscommerce.dto.OrderDTO;
import com.emarra.dscommerce.dto.OrderItemDTO;
import com.emarra.dscommerce.entities.Order;
import com.emarra.dscommerce.entities.OrderItem;
import com.emarra.dscommerce.entities.OrderStatus;
import com.emarra.dscommerce.entities.Product;
import com.emarra.dscommerce.repositories.OrderItemRepository;
import com.emarra.dscommerce.repositories.OrderRepository;
import com.emarra.dscommerce.repositories.ProductRepository;
import com.emarra.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired private OrderRepository orderRepository;
	@Autowired private ProductRepository productRepository;
	@Autowired private OrderItemRepository orderItemRepository;
	@Autowired private UserService user;
	@Autowired private AuthService authService;
	
	
	@Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado."));
        
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		order.setClient(user.authenticated());
				
		for(OrderItemDTO itemDto : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDto.getProductId());
			OrderItem orderItem = new OrderItem(order, product,itemDto.getQuantity(), product.getPrice());
			order.getItems().add(orderItem);
		}
		
		orderRepository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
		
	}

}
