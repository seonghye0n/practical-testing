package sample.cafekiosk.spring.api.service.product;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public void getSellingProducts() {

	}
}
