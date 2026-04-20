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
import com.wm.entity.SalesOrder;
import com.wm.entity.Partner;
import com.wm.repository.SalesOrderRepository;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class SalesOrderServiceTest {

	@Autowired
	private SalesOrderService salesOrderService;
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	private Partner testPartner;
	
	private List<Long> testSalesOrderIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = TestHelper.createTestPartner(partnerRepository);
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> salesOrderService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		SalesOrder testSalesOrder = TestHelper.createTestSalesOrder(testPartner, salesOrderRepository, testSalesOrderIdList);
		TestHelper.assertEqualsSalesOrder(testSalesOrder, salesOrderService.findById(testSalesOrder.getSalesOrderId()));
    }
    
    @Test
    void testCreate_AlreadyExists() {
    	SalesOrder testSalesOrder = TestHelper.createTestSalesOrder(testPartner, salesOrderRepository, testSalesOrderIdList);
    	assertThrows(RuntimeException.class, () -> salesOrderService.create(testSalesOrder));
    }
    
    @Test
    void testCreate_NotExists() {
    	SalesOrder testSalesOrder = SalesOrder.builder()
				.salesOrderNumber(String.valueOf(System.currentTimeMillis()))
				.partner(testPartner)
				.build();
	
    	
    	
    	testSalesOrder = salesOrderService.create(testSalesOrder);
    	testSalesOrderIdList.add(testSalesOrder.getSalesOrderId());
    	
    	SalesOrder expectedSalesOrder = TestHelper.createTestSalesOrder(testPartner);
    	expectedSalesOrder.setSalesOrderId(testSalesOrder.getSalesOrderId());
    	expectedSalesOrder.setSalesOrderNumber(testSalesOrder.getSalesOrderNumber());
    	expectedSalesOrder.setSalesOrderStatus("NEW");
    	
    	
    	TestHelper.assertEqualsSalesOrder(expectedSalesOrder, testSalesOrder);
    }
    
    @Test
    void testUpdate() {
    	SalesOrder testSalesOrder = TestHelper.createTestSalesOrder(testPartner, salesOrderRepository, testSalesOrderIdList);
    	salesOrderService.update(testSalesOrder.getSalesOrderId(), testSalesOrder);
    	testSalesOrder = salesOrderService.findById(testSalesOrder.getSalesOrderId());
    	
    	
    	SalesOrder expectedSalesOrder = TestHelper.createTestSalesOrder(testPartner);
    	expectedSalesOrder.setSalesOrderId(testSalesOrder.getSalesOrderId());
    	expectedSalesOrder.setSalesOrderNumber(testSalesOrder.getSalesOrderNumber());
    	
    	TestHelper.assertEqualsSalesOrder(expectedSalesOrder, testSalesOrder);
    }
    
    
	@AfterAll
	void deleteTestRecords() {
		salesOrderRepository.deleteAllById(testSalesOrderIdList);
    	partnerRepository.delete(testPartner);
	}
	
}