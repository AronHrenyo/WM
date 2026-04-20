package com.wm.service;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.wm.entity.Partner;
import com.wm.entity.PurchaseOrder;
import com.wm.entity.PurchaseOrderLine;
import com.wm.repository.PartnerRepository;
import com.wm.repository.PurchaseOrderLineRepository;
import com.wm.repository.PurchaseOrderRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class PurchaseOrderLineServiceTest {

	@Autowired
	private PurchaseOrderLineService purchaseOrderLineService;
	
	@Autowired
	private PurchaseOrderLineRepository purchaseOrderLineRepository;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private PartnerRepository partnerRepository; 
	
	private Partner testPartner;
	private PurchaseOrder testPurchaseOrder;
	
	private List<Long> testPurchaseOrderLineIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = TestHelper.createTestPartner(partnerRepository);
		testPurchaseOrder = TestHelper.createTestPurchaseOrder(testPartner, purchaseOrderRepository);
		
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> purchaseOrderLineService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		PurchaseOrderLine testPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder, purchaseOrderLineRepository, testPurchaseOrderLineIdList);
		TestHelper.assertEqualsPurchaseOrderLine(testPurchaseOrderLine, purchaseOrderLineService.findById(testPurchaseOrderLine.getPurchaseOrderLineId()));
    }
    
    
    @Test
    void testCreate(){
    	PurchaseOrderLine testPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder);
    	testPurchaseOrderLine = purchaseOrderLineService.create(testPurchaseOrderLine);
    	testPurchaseOrderLineIdList.add(testPurchaseOrderLine.getPurchaseOrderLineId());
    	
    	PurchaseOrderLine expectedPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder);
    	expectedPurchaseOrderLine.setPurchaseOrderLineId(testPurchaseOrderLine.getPurchaseOrderLineId());
    	purchaseOrderLineService.calculateLineSums(expectedPurchaseOrderLine);
    	TestHelper.assertEqualsPurchaseOrderLine(expectedPurchaseOrderLine, testPurchaseOrderLine);
    }
    
    @Test
    void testUpdate() {
    	PurchaseOrderLine testPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder, purchaseOrderLineRepository, testPurchaseOrderLineIdList);
    	PurchaseOrderLine expectedPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder);
    	expectedPurchaseOrderLine.setPurchaseOrderLineId(testPurchaseOrderLine.getPurchaseOrderLineId());
    	expectedPurchaseOrderLine.setPurchaseOrderLineQuantity(10);
    	expectedPurchaseOrderLine.setPurchaseOrderLineVatSum(BigDecimal.ZERO);
    	purchaseOrderLineService.calculateLineSums(expectedPurchaseOrderLine);
    	
    	testPurchaseOrderLine = purchaseOrderLineService.update(testPurchaseOrderLine.getPurchaseOrderLineId(), expectedPurchaseOrderLine);
    	TestHelper.assertEqualsPurchaseOrderLine(expectedPurchaseOrderLine, testPurchaseOrderLine);
    }
    
    @Test
    void testDelete() {
    	PurchaseOrderLine testPurchaseOrderLine = TestHelper.createTestPurchaseOrderLine(testPurchaseOrder, purchaseOrderLineRepository, testPurchaseOrderLineIdList);
    	TestHelper.assertEqualsPurchaseOrderLine(testPurchaseOrderLine, purchaseOrderLineRepository.findById(testPurchaseOrderLine.getPurchaseOrderLineId()).get());
    	purchaseOrderLineService.delete(testPurchaseOrderLine.getPurchaseOrderLineId());
    	assertTrue(purchaseOrderLineRepository.findById(testPurchaseOrderLine.getPurchaseOrderLineId()).isEmpty());
    	testPurchaseOrderLineIdList.remove(testPurchaseOrderLine.getPurchaseOrderLineId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		purchaseOrderLineRepository.deleteAllById(testPurchaseOrderLineIdList);
		purchaseOrderRepository.delete(testPurchaseOrder);
		partnerRepository.delete(testPartner);    	
	}
	
}