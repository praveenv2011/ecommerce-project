package com.ecom.order.service;

import com.ecom.order.domain.Order;
import com.ecom.order.domain.OrderItem;
import com.ecom.order.domain.Payment;
import com.ecom.order.dto.*;
import com.ecom.order.exception.ApiException;
import com.ecom.order.exception.ResourceNotFoundException;
import com.ecom.order.feignClient.AddressClient;
import com.ecom.order.feignClient.CartClient;
import com.ecom.order.feignClient.ProductClient;
import com.ecom.order.repository.OrderItemRepository;
import com.ecom.order.repository.OrderRepository;
import com.ecom.order.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartClient cartClient;

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public OrderDTO placeOrder(Long userid, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        CartDTO cartDTO = cartClient.getCartByUserId(userid);

        if (cartDTO == null) {
            throw new ResourceNotFoundException("cart", "userid", userid);
        }

        AddressDTO address = addressClient.getAddressById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("addressId", "addressId", addressId));

        Order order = new Order();
        order.setUserId(userid);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(cartDTO.getTotalPrice());
        order.setOrderStatus("Order Confirmed");
        order.setAddressId(address.getAddressId());

        // Saving the payment
        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment); // Save payment

        // Set saved payment to order
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        // Setting order items
        List<ProductDTO> cartItems = cartDTO.getProductinforamtion();
        if (cartItems.isEmpty()) {
            throw new ApiException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (ProductDTO productInCart : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productInCart.getProductId());
            orderItem.setQuantity(productInCart.getQuantity());
            orderItem.setDiscount(productInCart.getDiscount());
            orderItem.setOrderedProductPrice(productInCart.getSpecialPrice());
            orderItem.setOrder(savedOrder);

            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        // Update product quantity and clear cart
        cartDTO.getProductinforamtion().forEach(productInCart -> {
            int quantityOfProductInTheCart = productInCart.getQuantity();
            ProductDTO product = productClient.getProductById(productInCart.getProductId());

            product.setQuantity(product.getQuantity() - quantityOfProductInTheCart);
            productClient.updateProductById(product, productInCart.getProductId());

            cartClient.deleteProductInCartById(cartDTO.getUserId(), productInCart.getProductId());
        });

        // Map to OrderDTO
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderDTO.setAddress(address.getBuildingName() + "," + address.getStreet() + "," + address.getCity() + "," + address.getState() + "-" + address.getPincode());

        orderItems.forEach(orderItem -> orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class)));

        return orderDTO;
    }

    public List<OrderDTO> getByUserId(Long userid) {

        List<Order> orders = orderRepository.findAllByUserId(userid);
        List<OrderDTO> orderDTOS = orders.stream().map(order -> modelMapper.map(order,OrderDTO.class)).toList();

        return orderDTOS;
    }
}
