package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();

		//Product
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

		List<Product> duplicateProducts = findProductsBy(productNumbers,
			products);

		Order order = Order.create(duplicateProducts, registeredDateTime);

		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private static List<Product> findProductsBy(List<String> productNumbers, List<Product> products) {
		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		List<Product> duplicateProducts = productNumbers.stream()
			.map(productMap::get)
			.collect(Collectors.toList());
		return duplicateProducts;
	}
}
