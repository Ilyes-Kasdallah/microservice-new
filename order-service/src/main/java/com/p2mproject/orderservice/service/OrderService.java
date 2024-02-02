package com.p2mproject.orderservice.service;

import com.p2mproject.orderservice.dto.InventoryResponse;
import com.p2mproject.orderservice.dto.OrderLineItemsDto;
import com.p2mproject.orderservice.dto.OrderRequest;
import com.p2mproject.orderservice.model.Order;
import com.p2mproject.orderservice.model.OrderLineItems;
import com.p2mproject.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        // call inventory service and place order if product is in stock
       InventoryResponse[] inventoryResponses = webClientBuilder.build().get() // to get instance of web client
                                 .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                 .retrieve()
                                 .bodyToMono(InventoryResponse[].class)
                                 .block();// to make a synchron request
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock ! Please try again later");
        }
    }
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
         OrderLineItems orderLineItems = new OrderLineItems();
         orderLineItems.setPrice(orderLineItemsDto.getPrice());
         orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
         orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
         return orderLineItems;
    }
}
