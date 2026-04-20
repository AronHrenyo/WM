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
import com.wm.entity.SalesOrder;
import com.wm.entity.SalesOrderLine;
import com.wm.entity.Partner;
import com.wm.repository.SalesOrderLineRepository;
import com.wm.repository.SalesOrderRepository;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class SalesOrderLineServiceTest {

	@Autowired
	private SalesOrderLineService salesOrderLineService;
	
	@Autowired
	private SalesOrderLineRepository salesOrderLineRepository;
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private PartnerRepository partnerRepository; 
	
	private Partner testPartner;
	private SalesOrder testSalesOrder;
	
	private List<Long> testSalesOrderLineIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = TestHelper.createTestPartner(partnerRepository);
		testSalesOrder = TestHelper.createTestSalesOrder(testPartner, salesOrderRepository);
		
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> salesOrderLineService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		SalesOrderLine testSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder, salesOrderLineRepository, testSalesOrderLineIdList);
		TestHelper.assertEqualsSalesOrderLine(testSalesOrderLine, salesOrderLineService.findById(testSalesOrderLine.getSalesOrderLineId()));
    }
    
    
    @Test
    void testCreate(){
    	SalesOrderLine testSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder);
    	testSalesOrderLine = salesOrderLineService.create(testSalesOrderLine);
    	testSalesOrderLineIdList.add(testSalesOrderLine.getSalesOrderLineId());
    	
    	SalesOrderLine expectedSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder);
    	expectedSalesOrderLine.setSalesOrderLineId(testSalesOrderLine.getSalesOrderLineId());
    	salesOrderLineService.calculateLineSums(expectedSalesOrderLine);
    	TestHelper.assertEqualsSalesOrderLine(expectedSalesOrderLine, testSalesOrderLine);
    }
    
    @Test
    void testUpdate() {
    	SalesOrderLine testSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder, salesOrderLineRepository, testSalesOrderLineIdList);
    	SalesOrderLine expectedSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder);
    	expectedSalesOrderLine.setSalesOrderLineId(testSalesOrderLine.getSalesOrderLineId());
    	expectedSalesOrderLine.setSalesOrderLineQuantity(10);
    	expectedSalesOrderLine.setSalesOrderLineVatSum(BigDecimal.ZERO);
    	salesOrderLineService.calculateLineSums(expectedSalesOrderLine);
    	
    	testSalesOrderLine = salesOrderLineService.update(testSalesOrderLine.getSalesOrderLineId(), expectedSalesOrderLine);
    	TestHelper.assertEqualsSalesOrderLine(expectedSalesOrderLine, testSalesOrderLine);
    }
    
    @Test
    void testDelete() {
    	SalesOrderLine testSalesOrderLine = TestHelper.createTestSalesOrderLine(testSalesOrder, salesOrderLineRepository, testSalesOrderLineIdList);
    	TestHelper.assertEqualsSalesOrderLine(testSalesOrderLine, salesOrderLineRepository.findById(testSalesOrderLine.getSalesOrderLineId()).get());
    	salesOrderLineService.delete(testSalesOrderLine.getSalesOrderLineId());
    	assertTrue(salesOrderLineRepository.findById(testSalesOrderLine.getSalesOrderLineId()).isEmpty());
    	testSalesOrderLineIdList.remove(testSalesOrderLine.getSalesOrderLineId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		salesOrderLineRepository.deleteAllById(testSalesOrderLineIdList);
		salesOrderRepository.delete(testSalesOrder);
		partnerRepository.delete(testPartner);    	
	}
	
}