package com.wm.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wm.TestHelper;
import com.wm.WarehouseManagementApplication;
import com.wm.entity.Product;
import com.wm.repository.ProductRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	private List<Long> testProductIdList = new ArrayList<Long>();
	
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> productService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		Product testProduct = TestHelper.createTestProduct(productRepository, testProductIdList);
		TestHelper.assertEqualsProduct(testProduct, productService.findById(testProduct.getProductId()));
    }
    
    @Test
    void testCreate_NonUniqueSku(){
    	Product product = TestHelper.createTestProduct();
    	product.setProductSku("test");
    	product = productRepository.save(product);

    	Product testProduct = TestHelper.createTestProduct();
    	testProduct.setProductSku("test");
    	assertThrows(RuntimeException.class, () -> productService.create(testProduct));
    	productRepository.delete(product);
    	testProductIdList.remove(product.getProductId());
    }
    @Test
    void testCreate_UniqueSku(){
    	Product testProduct = TestHelper.createTestProduct();
    	testProduct.setProductSku("test");
    	testProduct = productService.create(testProduct);
    	testProductIdList.add(testProduct.getProductId());
    	
    	Product expectedProduct = TestHelper.createTestProduct();
    	expectedProduct.setProductId(testProduct.getProductId());
    	expectedProduct.setProductSku("test");
    	TestHelper.assertEqualsProduct(expectedProduct, testProduct);
    }
    
    
    @Test
    void testUpdate() {
    	Product testProduct = TestHelper.createTestProduct(productRepository, testProductIdList);
    	Product expectedProduct = TestHelper.createTestProduct();
    	expectedProduct.setProductId(testProduct.getProductId());
    	expectedProduct.setProductSku("test");
    	expectedProduct.setProductSalesPrice(BigDecimal.TEN);
    	
    	testProduct = productService.update(testProduct.getProductId(), expectedProduct);
    	TestHelper.assertEqualsProduct(expectedProduct, testProduct);
    }
    
    @Test
    void testDelete() {
    	Product testProduct = TestHelper.createTestProduct(productRepository, testProductIdList);
    	TestHelper.assertEqualsProduct(testProduct, productRepository.findById(testProduct.getProductId()).get());
    	productService.delete(testProduct.getProductId());
    	assertTrue(productRepository.findById(testProduct.getProductId()).isEmpty());
    	testProductIdList.remove(testProduct.getProductId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		productRepository.deleteAllById(testProductIdList);
	}
	
}