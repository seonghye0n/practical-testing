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
		Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000, SELLING);

		Product product2 = createProduct("002", HANDMADE, "카페라떼", 4500, HOLD);

		Product product3 = createProduct("003", HANDMADE, "팥빙수", 7000, STOP_SELLING);

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
		Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000, SELLING);

		Product product2 = createProduct("002", HANDMADE, "카페라떼", 4500, HOLD);

		Product product3 = createProduct("003", HANDMADE, "팥빙수", 7000, STOP_SELLING);

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

	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
	@Test
	void findLatestProductNumber() {
		//given
		Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000, SELLING);

		Product product2 = createProduct("002", HANDMADE, "카페라떼", 4500, HOLD);

		String targetProductNumber = "003";
		Product product3 = createProduct(targetProductNumber, HANDMADE, "팥빙수", 7000, STOP_SELLING);

		productRepository.saveAll(List.of(product1, product2, product3));

		//when
		String latestProductNumber = productRepository.findLatestProductNumber();

		//then
		assertThat(latestProductNumber).isEqualTo(targetProductNumber);
	}

	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
	@Test
	void findLatestProductNumberWhenProductIsEmpty() {
		//given

		//when
		String latestProductNumber = productRepository.findLatestProductNumber();

		//then
		assertThat(latestProductNumber).isNull();
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