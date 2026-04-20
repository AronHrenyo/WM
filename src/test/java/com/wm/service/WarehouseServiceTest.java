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
import com.wm.entity.Warehouse;
import com.wm.repository.WarehouseRepository;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {WarehouseManagementApplication.class}
)
@AutoConfigureMockMvc
public class WarehouseServiceTest {

	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private WarehouseRepository warehouseRepository;
	
	
	private List<Long> testWarehouseIdList = new ArrayList<Long>();
	
	
	@Test
	void testFindById_InvalidId() {
		assertThrows(RuntimeException.class, () -> warehouseService.findById(-1L));
	}
	
	
    @Test
    void testFindById_ValidId() {
		Warehouse testWarehouse = TestHelper.createTestWarehouse(warehouseRepository, testWarehouseIdList);
		assertEquals(testWarehouse, warehouseService.findById(testWarehouse.getWarehouseId()));
    }
    
    @Test
    void testCreate(){
    	Warehouse testWarehouse = TestHelper.createTestWarehouse();
    	testWarehouse = warehouseService.create(testWarehouse);
    	testWarehouseIdList.add(testWarehouse.getWarehouseId());
    	
    	Warehouse expectedWarehouse = TestHelper.createTestWarehouse();
    	expectedWarehouse.setWarehouseId(testWarehouse.getWarehouseId());
    	assertEquals(expectedWarehouse, testWarehouse);
    }
    
    
    @Test
    void testUpdate() {
    	Warehouse testWarehouse = TestHelper.createTestWarehouse(warehouseRepository, testWarehouseIdList);
    	Warehouse expectedWarehouse = TestHelper.createTestWarehouse();
    	expectedWarehouse.setWarehouseId(testWarehouse.getWarehouseId());
    	
    	testWarehouse = warehouseService.update(testWarehouse.getWarehouseId(), expectedWarehouse);
    	assertEquals(expectedWarehouse, testWarehouse);
    }
    
    @Test
    void testDelete() {
    	Warehouse testWarehouse = TestHelper.createTestWarehouse(warehouseRepository, testWarehouseIdList);
    	assertEquals(testWarehouse, warehouseRepository.findById(testWarehouse.getWarehouseId()).get());
    	warehouseService.delete(testWarehouse.getWarehouseId());
    	assertTrue(warehouseRepository.findById(testWarehouse.getWarehouseId()).isEmpty());
    	testWarehouseIdList.remove(testWarehouse.getWarehouseId());
    }
    
	@AfterAll
	void deleteTestRecords() {
		warehouseRepository.deleteAllById(testWarehouseIdList);
	}
	
}