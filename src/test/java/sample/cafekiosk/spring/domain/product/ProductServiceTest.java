package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

public class ProductServiceTest extends IntegrationTestSupport {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가 값이다.")
	@Test
	void createProduct() {
		//given
		Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000, SELLING);
		productRepository.save(product1);

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.name("카푸치노")
			.price(5000)
			.sellingStatus(SELLING)
			.type(HANDMADE)
			.build();

		//when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.contains("002", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.containsExactlyInAnyOrder(
				tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
				tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
			);
	}

	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다. ")
	@Test
	void createProductWhenProductIsEmpty() {
		//given

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.name("카푸치노")
			.price(5000)
			.sellingStatus(SELLING)
			.type(HANDMADE)
			.build();

		//when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.contains("001", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.contains(tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
	}

	private Product createProduct(String productNumber, ProductType productType, String name, int price,
		ProductSellingStatus sellingStatus) {
		return Product.builder()
			.productNumber(productNumber)
			.type(productType)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}
