package com.emarra.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.emarra.dscommerce.entities.Order;
import com.emarra.dscommerce.entities.OrderItem;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {
	
	private Long id;
	private Instant moment;
	private String status;
	private ClientDTO client;
	private PaymentDTO payment;
	@NotEmpty(message = "Deve ter pelo menos um item.")
	private List<OrderItemDTO> items = new ArrayList<OrderItemDTO>();

	public OrderDTO() {
	}

	public OrderDTO(Long id, Instant moment, String status, ClientDTO client, PaymentDTO payment) {
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
	}
	
	public OrderDTO(Order order) {
		id = order.getId();
		moment = order.getMoment();
		status = order.getStatus().name();
		client = new ClientDTO(order.getClient());
		payment = (order.getPayment() == null) ? null : new PaymentDTO(order.getPayment());
		
		for (OrderItem item: order.getItems()) {
			items.add(new OrderItemDTO(item));
		}
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public String getStatus() {
		return status;
	}

	public ClientDTO getClient() {
		return client;
	}

	public PaymentDTO getPayment() {
		return payment;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}	
	
	public double getTotal() {
		double soma = 0;
		for (OrderItemDTO item: items) {
			soma += item.getSubTotal();
		}
		return soma;
	}

}
