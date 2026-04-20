package com.wm.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.wm.entity.Partner;
import com.wm.repository.PartnerRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class PartnerServiceTest {

	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	
	private List<Long> testPartnerIdList = new ArrayList<Long>();
	
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> partnerService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		Partner testPartner = TestHelper.createTestPartner(partnerRepository, testPartnerIdList);
		assertEquals(testPartner, partnerService.findById(testPartner.getPartnerId()));
    }
    
    @Test
    void testCreate(){
    	Partner testPartner = TestHelper.createTestPartner();
    	testPartner = partnerService.create(testPartner);
    	testPartnerIdList.add(testPartner.getPartnerId());
    	
    	Partner expectedPartner = TestHelper.createTestPartner();
    	expectedPartner.setPartnerId(testPartner.getPartnerId());
    	assertEquals(expectedPartner, testPartner);
    }
    
    @Test
    void testUpdate() {
    	Partner testPartner = TestHelper.createTestPartner(partnerRepository, testPartnerIdList);
    	Partner expectedPartner = TestHelper.createTestPartner();
    	expectedPartner.setPartnerId(testPartner.getPartnerId());
    	expectedPartner.setPartnerAddress("test");
    	
    	testPartner = partnerService.update(testPartner.getPartnerId(), expectedPartner);
    	assertEquals(expectedPartner, testPartner);
    }
    
    @Test
    void testDelete() {
    	Partner testPartner = TestHelper.createTestPartner(partnerRepository, testPartnerIdList);
    	assertEquals(testPartner, partnerRepository.findById(testPartner.getPartnerId()).get());
    	partnerService.delete(testPartner.getPartnerId());
    	assertTrue(partnerRepository.findById(testPartner.getPartnerId()).isEmpty());
    	testPartnerIdList.remove(testPartner.getPartnerId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		partnerRepository.deleteAllById(testPartnerIdList);
	}
	
}