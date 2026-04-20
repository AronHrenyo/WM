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
import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.entity.Partner;
import com.wm.repository.InvoiceLineRepository;
import com.wm.repository.InvoiceRepository;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class InvoiceLineServiceTest {

	@Autowired
	private InvoiceLineService invoiceLineService;
	
	@Autowired
	private InvoiceLineRepository invoiceLineRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private PartnerRepository partnerRepository; 
	
	private Partner testPartner;
	private Invoice testInvoice;
	
	private List<Long> testInvoiceLineIdList = new ArrayList<Long>();
	
	
	@BeforeAll
	void createTestRecords() {
		testPartner = TestHelper.createTestPartner(partnerRepository);
		testInvoice = TestHelper.createTestInvoice(testPartner, invoiceRepository);
		
	}
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> invoiceLineService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		InvoiceLine testInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice, invoiceLineRepository, testInvoiceLineIdList);
		TestHelper.assertEqualsInvoiceLine(testInvoiceLine, invoiceLineService.findById(testInvoiceLine.getInvoiceLineId()));
    }
    
    
    @Test
    void testCreate(){
    	InvoiceLine testInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice);
    	testInvoiceLine = invoiceLineService.create(testInvoiceLine);
    	testInvoiceLineIdList.add(testInvoiceLine.getInvoiceLineId());
    	
    	InvoiceLine expectedInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice);
    	expectedInvoiceLine.setInvoiceLineId(testInvoiceLine.getInvoiceLineId());
    	invoiceLineService.calculateLineSums(expectedInvoiceLine);
    	TestHelper.assertEqualsInvoiceLine(expectedInvoiceLine, testInvoiceLine);
    }
    
    @Test
    void testUpdate() {
    	InvoiceLine testInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice, invoiceLineRepository, testInvoiceLineIdList);
    	InvoiceLine expectedInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice);
    	expectedInvoiceLine.setInvoiceLineId(testInvoiceLine.getInvoiceLineId());
    	expectedInvoiceLine.setInvoiceLineQuantity(10);
    	expectedInvoiceLine.setInvoiceLineVatSum(BigDecimal.ZERO);
    	invoiceLineService.calculateLineSums(expectedInvoiceLine);
    	
    	testInvoiceLine = invoiceLineService.update(testInvoiceLine.getInvoiceLineId(), expectedInvoiceLine);
    	TestHelper.assertEqualsInvoiceLine(expectedInvoiceLine, testInvoiceLine);
    }
    
    @Test
    void testDelete() {
    	InvoiceLine testInvoiceLine = TestHelper.createTestInvoiceLine(testInvoice, invoiceLineRepository, testInvoiceLineIdList);
    	TestHelper.assertEqualsInvoiceLine(testInvoiceLine, invoiceLineRepository.findById(testInvoiceLine.getInvoiceLineId()).get());
    	invoiceLineService.delete(testInvoiceLine.getInvoiceLineId());
    	assertTrue(invoiceLineRepository.findById(testInvoiceLine.getInvoiceLineId()).isEmpty());
    	testInvoiceLineIdList.remove(testInvoiceLine.getInvoiceLineId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		invoiceLineRepository.deleteAllById(testInvoiceLineIdList);
		invoiceRepository.delete(testInvoice);
		partnerRepository.delete(testPartner);    	
	}
	
}