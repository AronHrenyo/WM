package com.wm.service;



import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import com.wm.entity.PurchaseOrder;
import com.wm.entity.Partner;
import com.wm.repository.PurchaseOrderRepository;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class PurchaseOrderServiceTest {

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	private Partner testPartner;
	
	private List<Long> testPurchaseOrderIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = TestHelper.createTestPartner(partnerRepository);
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> purchaseOrderService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		PurchaseOrder testPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner, purchaseOrderRepository, testPurchaseOrderIdList);
		TestHelper.assertEqualsPurchaseOrder(testPurchaseOrder, purchaseOrderService.findById(testPurchaseOrder.getPurchaseOrderId()));
    }
    
    @Test
    void testCreate_AlreadyExists() {
    	PurchaseOrder testPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner, purchaseOrderRepository, testPurchaseOrderIdList);
    	assertThrows(RuntimeException.class, () -> purchaseOrderService.create(testPurchaseOrder));
    }
    
    @Test
    void testCreate_NotExists() {
    	PurchaseOrder testPurchaseOrder = PurchaseOrder.builder()
				.purchaseOrderNumber(String.valueOf(System.currentTimeMillis()))
				.partner(testPartner)
				.build();
	
    	
    	
    	testPurchaseOrder = purchaseOrderService.create(testPurchaseOrder);
    	testPurchaseOrderIdList.add(testPurchaseOrder.getPurchaseOrderId());
    	
    	PurchaseOrder expectedPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner);
    	expectedPurchaseOrder.setPurchaseOrderId(testPurchaseOrder.getPurchaseOrderId());
    	expectedPurchaseOrder.setPurchaseOrderNumber(testPurchaseOrder.getPurchaseOrderNumber());
    	expectedPurchaseOrder.setPurchaseOrderStatus("NEW");
    	
    	
    	TestHelper.assertEqualsPurchaseOrder(expectedPurchaseOrder, testPurchaseOrder);
    }
    
    @Test
    void testUpdate() {
    	PurchaseOrder testPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner, purchaseOrderRepository, testPurchaseOrderIdList);
    	purchaseOrderService.update(testPurchaseOrder.getPurchaseOrderId(), testPurchaseOrder);
    	testPurchaseOrder = purchaseOrderService.findById(testPurchaseOrder.getPurchaseOrderId());
    	
    	
    	PurchaseOrder expectedPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner);
    	expectedPurchaseOrder.setPurchaseOrderId(testPurchaseOrder.getPurchaseOrderId());
    	expectedPurchaseOrder.setPurchaseOrderNumber(testPurchaseOrder.getPurchaseOrderNumber());
    	
    	TestHelper.assertEqualsPurchaseOrder(expectedPurchaseOrder, testPurchaseOrder);
    }
    
    
	@AfterAll
	void deleteTestRecords() {
		purchaseOrderRepository.deleteAllById(testPurchaseOrderIdList);
    	partnerRepository.delete(testPartner);
	}
	
}