package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductNumberFactoryTest {

	@Autowired
	private ProductNumberFactory productNumberFactory;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("상품이 없다면 상품번호는 001부터 시작한다.")
	@Test
	void createNextProductNumberWithFirst() {
		//given

		//when
		String result = productNumberFactory.createNextProductNumber();

		//then
		Assertions.assertThat(result).isEqualTo("001");
	}

	@DisplayName("상품번호는 001, 002 순으로 생성한다.")
	@Test
	void createNextProductNumber() {
		//given
		Product product = Product.builder()
			.type(ProductType.HANDMADE)
			.sellingStatus(ProductSellingStatus.SELLING)
			.price(1000)
			.name("마카롱")
			.productNumber(productNumberFactory.createNextProductNumber())
			.build();

		productRepository.save(product);

		//when
		String result = productNumberFactory.createNextProductNumber();

		//then
		Assertions.assertThat(result).isEqualTo("002");
	}
}