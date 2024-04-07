package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {
	/*
		Persistence Layer
		- Data Access의 역할
		- 비즈니스 가공 로직 포함 X
		- Data에 대한 CRUD에만 집중한 레이어
	 */
	@Autowired
	private ProductRepository productRepository;

	@DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
	@Test
	void findAllBySellingStatusIn() {
		//given
		Product product1 = Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		Product product2 = Product.builder()
			.productNumber("002")
			.type(HANDMADE)
			.sellingStatus(HOLD)
			.name("카페라떼")
			.price(4500)
			.build();

		Product product3 = Product.builder()
			.productNumber("003")
			.type(HANDMADE)
			.sellingStatus(STOP_SELLING)
			.name("팥빙수")
			.price(7000)
			.build();

		productRepository.saveAll(List.of(product1, product2, product3));

		//when
		List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

		//then

		//extracting -> 필드 값 검증
		//containsExactlyInAnyOrder -> 순서 상관없이 해당 튜플과 일치한 애들 검증
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라떼", HOLD)
			);
	}

	@DisplayName("상품번호 리스트로 상품들을 조회한다.")
	@Test
	void findAllByProductNumberIn() {
		//given
		Product product1 = Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		Product product2 = Product.builder()
			.productNumber("002")
			.type(HANDMADE)
			.sellingStatus(HOLD)
			.name("카페라떼")
			.price(4500)
			.build();

		Product product3 = Product.builder()
			.productNumber("003")
			.type(HANDMADE)
			.sellingStatus(STOP_SELLING)
			.name("팥빙수")
			.price(7000)
			.build();

		productRepository.saveAll(List.of(product1, product2, product3));

		//when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

		//then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라떼", HOLD)
			);
	}
}