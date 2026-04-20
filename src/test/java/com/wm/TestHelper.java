package com.wm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.entity.Partner;
import com.wm.entity.Product;
import com.wm.entity.PurchaseOrder;
import com.wm.entity.PurchaseOrderLine;
import com.wm.entity.SalesOrder;
import com.wm.entity.SalesOrderLine;
import com.wm.entity.Warehouse;
import com.wm.repository.InvoiceLineRepository;
import com.wm.repository.InvoiceRepository;
import com.wm.repository.PartnerRepository;
import com.wm.repository.ProductRepository;
import com.wm.repository.PurchaseOrderLineRepository;
import com.wm.repository.PurchaseOrderRepository;
import com.wm.repository.SalesOrderLineRepository;
import com.wm.repository.SalesOrderRepository;
import com.wm.repository.WarehouseRepository;

public class TestHelper {
	public static void assertEqualsBigDecimal(BigDecimal expected, BigDecimal actual, String content) {
		assertNotNull(expected);
		assertNotNull(actual);
		
		assertEquals(0, expected.compareTo(actual), String.format("várt %s (%s) != tényleges %s (%s)",   content, expected.toPlainString(), content, actual.toPlainString()));
	}
	
	public static void assertEqualsInvoice(Invoice expected, Invoice actual) {
		assertEquals(expected.getInvoiceId(), actual.getInvoiceId());
		assertEquals(expected.getInvoiceNumber(), actual.getInvoiceNumber());
		assertEquals(expected.getInvoiceDate(), actual.getInvoiceDate());
		assertEquals(expected.getInvoiceStatus(), actual.getInvoiceStatus());
		assertEqualsBigDecimal(expected.getInvoiceNetSum(), actual.getInvoiceNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getInvoiceVatSum(), actual.getInvoiceVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getInvoiceGrossSum(), actual.getInvoiceGrossSum(), "gross sum");
	    assertEquals(expected.getPartner().getPartnerId(), actual.getPartner().getPartnerId());
	}
	
	public static void assertEqualsInvoiceLine(InvoiceLine expected, InvoiceLine actual) {
		assertEquals(expected.getInvoiceLineId(), actual.getInvoiceLineId());
		assertEquals(expected.getInvoice().getInvoiceId(), actual.getInvoice().getInvoiceId());
		assertEquals(expected.getProduct(), actual.getProduct());
		assertEqualsBigDecimal(expected.getInvoiceLinePrice(), actual.getInvoiceLinePrice(), "price");
		assertEquals(expected.getInvoiceLineVatKey(), actual.getInvoiceLineVatKey());
		assertEquals(expected.getInvoiceLineQuantity(), actual.getInvoiceLineQuantity());
		assertEqualsBigDecimal(expected.getInvoiceLineNetSum(), actual.getInvoiceLineNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getInvoiceLineVatSum(), actual.getInvoiceLineVatSum(), "price");
		assertEqualsBigDecimal(expected.getInvoiceLineGrossSum(), actual.getInvoiceLineGrossSum(), "price");
	}
	
	public static void assertEqualsProduct(Product expected, Product actual) {
		assertEquals(expected.getProductId(), actual.getProductId());
		assertEquals(expected.getProductSku(), actual.getProductSku());
		assertEquals(expected.getProductName(), actual.getProductName());
		assertEquals(expected.getProductCategory(), actual.getProductCategory());
		assertEqualsBigDecimal(expected.getProductPurchasePrice(), actual.getProductPurchasePrice(), "purchase price");
		assertEqualsBigDecimal(expected.getProductSalesPrice(), actual.getProductSalesPrice(), "sales price");
		assertEquals(expected.getProductVatKey(), actual.getProductVatKey());
	}
	
	public static void assertEqualsPurchaseOrder(PurchaseOrder expected, PurchaseOrder actual) {
		assertEquals(expected.getPurchaseOrderId(), actual.getPurchaseOrderId());
		assertEquals(expected.getPurchaseOrderNumber(), actual.getPurchaseOrderNumber());
		assertEquals(expected.getPurchaseOrderDate(), actual.getPurchaseOrderDate());
		assertEqualsBigDecimal(expected.getPurchaseOrderNetSum(), actual.getPurchaseOrderNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getPurchaseOrderVatSum(), actual.getPurchaseOrderVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getPurchaseOrderGrossSum(), actual.getPurchaseOrderGrossSum(), "gross sum");
	}
	
	public static void assertEqualsPurchaseOrderLine(PurchaseOrderLine expected, PurchaseOrderLine actual) {
		assertEquals(expected.getPurchaseOrderLineId(), actual.getPurchaseOrderLineId());
		assertEquals(expected.getPurchaseOrder().getPurchaseOrderId(), actual.getPurchaseOrder().getPurchaseOrderId());
		assertEquals(expected.getProduct(), actual.getProduct());
		assertEqualsBigDecimal(expected.getPurchaseOrderLinePrice(), actual.getPurchaseOrderLinePrice(), "price");
		assertEquals(expected.getPurchaseOrderLineVatKey(), actual.getPurchaseOrderLineVatKey());
		assertEquals(expected.getPurchaseOrderLineQuantity(), actual.getPurchaseOrderLineQuantity());
		assertEqualsBigDecimal(expected.getPurchaseOrderLineNetSum(), actual.getPurchaseOrderLineNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getPurchaseOrderLineVatSum(), actual.getPurchaseOrderLineVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getPurchaseOrderLineGrossSum(), actual.getPurchaseOrderLineGrossSum(), "gross sum");
	}
	
	public static void assertEqualsSalesOrder(SalesOrder expected, SalesOrder actual) {
		assertEquals(expected.getSalesOrderId(), actual.getSalesOrderId());
		assertEquals(expected.getSalesOrderNumber(), actual.getSalesOrderNumber());
		assertEquals(expected.getSalesOrderDate(), actual.getSalesOrderDate());
		assertEqualsBigDecimal(expected.getSalesOrderNetSum(), actual.getSalesOrderNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getSalesOrderVatSum(), actual.getSalesOrderVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getSalesOrderGrossSum(), actual.getSalesOrderGrossSum(), "gross sum");
	}
	
	public static void assertEqualsSalesOrderLine(SalesOrderLine expected, SalesOrderLine actual) {
		assertEquals(expected.getSalesOrderLineId(), actual.getSalesOrderLineId());
		assertEquals(expected.getSalesOrder().getSalesOrderId(), actual.getSalesOrder().getSalesOrderId());
		assertEquals(expected.getProduct(), actual.getProduct());
		assertEqualsBigDecimal(expected.getSalesOrderLinePrice(), actual.getSalesOrderLinePrice(), "price");
		assertEquals(expected.getSalesOrderLineVatKey(), actual.getSalesOrderLineVatKey());
		assertEquals(expected.getSalesOrderLineQuantity(), actual.getSalesOrderLineQuantity());
		assertEqualsBigDecimal(expected.getSalesOrderLineNetSum(), actual.getSalesOrderLineNetSum(), "net sum");
		assertEqualsBigDecimal(expected.getSalesOrderLineVatSum(), actual.getSalesOrderLineVatSum(), "vat sum");
		assertEqualsBigDecimal(expected.getSalesOrderLineGrossSum(), actual.getSalesOrderLineGrossSum(), "gross sum");
	}
	
	public static Invoice createTestInvoice(Partner testPartner) {
		return Invoice.builder()
    			.invoiceId(null)
    			.invoiceNumber(String.valueOf(System.currentTimeMillis()))
    			.invoiceDate(LocalDate.now())
    			.invoiceStatus("")
    			.invoiceNetSum(BigDecimal.ZERO)
    			.invoiceVatSum(BigDecimal.ZERO)
    			.invoiceGrossSum(BigDecimal.ZERO)
    			.partner(testPartner)
    			.lines(new ArrayList<InvoiceLine>())
    			.build();
		
	}
	
	public static Invoice createTestInvoice(Partner testPartner, InvoiceRepository invoiceRepository) {
		Invoice testInvoice = createTestInvoice(testPartner);
		testInvoice = invoiceRepository.save(testInvoice);
		return testInvoice;
	}
	
	public static Invoice createTestInvoice(Partner testPartner, InvoiceRepository invoiceRepository, List<Long> testInvoiceIdList) {
		Invoice testInvoice = createTestInvoice(testPartner, invoiceRepository);
		testInvoiceIdList.add(testInvoice.getInvoiceId());
		return testInvoice;
	}
	
	
	public static InvoiceLine createTestInvoiceLine(Invoice invoice) {
		
		return InvoiceLine.builder()
				.invoiceLineId(null)
				.invoice(invoice)
				.product(null)
				.invoiceLineVatKey(0)
				.invoiceLineQuantity(0)
				.invoiceLinePrice(BigDecimal.ZERO)
				.invoiceLineNetSum(BigDecimal.TEN)
				.invoiceLineVatSum(BigDecimal.TEN)
				.invoiceLineGrossSum(BigDecimal.TEN)
				.build();
	}
	
	public static InvoiceLine createTestInvoiceLine(Invoice invoice, InvoiceLineRepository invoiceLineRepository, List<Long> testInvoiceLineIdList) {
		InvoiceLine testInvoiceLine = invoiceLineRepository.save(createTestInvoiceLine(invoice));
		testInvoiceLineIdList.add(testInvoiceLine.getInvoiceLineId());
		return testInvoiceLine;
	}
	
	
	public static Partner createTestPartner() {
		return Partner.builder()
				.partnerId(null)
				.partnerName("")
				.partnerEmail("")
				.partnerTelephone("")
				.partnerAddress("")
				.build();
	}
	
	public static Partner createTestPartner(PartnerRepository partnerRepository) {
		Partner testPartner = partnerRepository.save(createTestPartner());
		return testPartner;
	}
	
	public static Partner createTestPartner(PartnerRepository partnerRepository, List<Long> testPartnerIdList) {
		Partner testPartner = createTestPartner(partnerRepository);
		testPartnerIdList.add(testPartner.getPartnerId());
		return testPartner;
	}
	
	
	public static Product createTestProduct() {
		return Product.builder()
				.productId(null)
				.productSku("")
				.productName("")
				.productCategory("")
				.productPurchasePrice(BigDecimal.ZERO)
				.productSalesPrice(BigDecimal.ZERO)
				.productVatKey(0)
				.build();
	}
	
	public static Product createTestProduct(ProductRepository productRepository) {
		Product testProduct = productRepository.save(createTestProduct());
		return testProduct;
	}
	
	public static Product createTestProduct(ProductRepository productRepository, List<Long> testProductIdList) {
		Product testProduct = createTestProduct(productRepository);
		testProductIdList.add(testProduct.getProductId());
		return testProduct;
	}
	
	
	public static PurchaseOrder createTestPurchaseOrder(Partner partner) {
		return PurchaseOrder.builder()
				.purchaseOrderId(null)
				.purchaseOrderNumber("")
				.purchaseOrderDate(LocalDate.now())
				.purchaseOrderStatus("")
				.purchaseOrderNetSum(BigDecimal.ZERO)
				.purchaseOrderVatSum(BigDecimal.ZERO)
				.purchaseOrderGrossSum(BigDecimal.ZERO)
				.partner(partner)
				.build();
	}
	
	public static PurchaseOrder createTestPurchaseOrder(Partner partner, PurchaseOrderRepository purchaseOrderRepository) {
		PurchaseOrder testPurchaseOrder = purchaseOrderRepository.save(createTestPurchaseOrder(partner));
		return testPurchaseOrder;
	}
	
	public static PurchaseOrder createTestPurchaseOrder(Partner partner, PurchaseOrderRepository purchaseOrderRepository, List<Long> testPurchaseOrderIdList) {
		PurchaseOrder testPurchaseOrder = createTestPurchaseOrder(partner, purchaseOrderRepository);
		testPurchaseOrderIdList.add(testPurchaseOrder.getPurchaseOrderId());
		return testPurchaseOrder;
	}
	
	
	public static PurchaseOrderLine createTestPurchaseOrderLine(PurchaseOrder purchaseOrder) {
		return PurchaseOrderLine.builder()
				.purchaseOrderLineId(null)
				.purchaseOrder(purchaseOrder)
				.product(null)
				.purchaseOrderLinePrice(BigDecimal.ZERO)
				.purchaseOrderLineVatKey(0)
				.purchaseOrderLineQuantity(0)
				.purchaseOrderLineNetSum(BigDecimal.TEN)
				.purchaseOrderLineVatSum(BigDecimal.TEN)
				.purchaseOrderLineGrossSum(BigDecimal.TEN)
				.build();
	}
	
	public static PurchaseOrderLine createTestPurchaseOrderLine(PurchaseOrder purchaseOrder, PurchaseOrderLineRepository purchaseOrderLineRepository) {
		PurchaseOrderLine testPurchaseOrderLine = purchaseOrderLineRepository.save(createTestPurchaseOrderLine(purchaseOrder));
		return testPurchaseOrderLine;
	}
	
	public static PurchaseOrderLine createTestPurchaseOrderLine(PurchaseOrder purchaseOrder, PurchaseOrderLineRepository purchaseOrderLineRepository, List<Long> testPurchaseOrderIdList) {
		PurchaseOrderLine testPurchaseOrderLine = createTestPurchaseOrderLine(purchaseOrder, purchaseOrderLineRepository);
		testPurchaseOrderIdList.add(testPurchaseOrderLine.getPurchaseOrderLineId());
		return testPurchaseOrderLine;
	}
	
	
	public static SalesOrder createTestSalesOrder(Partner partner) {
		return SalesOrder.builder()
				.salesOrderId(null)
				.salesOrderNumber("")
				.salesOrderDate(LocalDate.now())
				.salesOrderStatus("")
				.salesOrderNetSum(BigDecimal.ZERO)
				.salesOrderVatSum(BigDecimal.ZERO)
				.salesOrderGrossSum(BigDecimal.ZERO)
				.partner(partner)
				.build();
	}
	
	public static SalesOrder createTestSalesOrder(Partner partner, SalesOrderRepository salesOrderRepository) {
		SalesOrder testSalesOrder = salesOrderRepository.save(createTestSalesOrder(partner));
		return testSalesOrder;
	}
	
	public static SalesOrder createTestSalesOrder(Partner partner, SalesOrderRepository salesOrderRepository, List<Long> testSalesOrderIdList) {
		SalesOrder testSalesOrder = createTestSalesOrder(partner, salesOrderRepository);
		testSalesOrderIdList.add(testSalesOrder.getSalesOrderId());
		return testSalesOrder;
	}
	
	
	public static SalesOrderLine createTestSalesOrderLine(SalesOrder salesOrder) {
		return SalesOrderLine.builder()
				.salesOrderLineId(null)
				.salesOrder(salesOrder)
				.product(null)
				.salesOrderLinePrice(BigDecimal.ZERO)
				.salesOrderLineVatKey(0)
				.salesOrderLineQuantity(0)
				.salesOrderLineNetSum(BigDecimal.TEN)
				.salesOrderLineVatSum(BigDecimal.TEN)
				.salesOrderLineGrossSum(BigDecimal.TEN)
				.build();
	}
	
	public static SalesOrderLine createTestSalesOrderLine(SalesOrder salesOrder, SalesOrderLineRepository salesOrderLineRepository) {
		SalesOrderLine testSalesOrderLine = salesOrderLineRepository.save(createTestSalesOrderLine(salesOrder));
		return testSalesOrderLine;
	}
	
	public static SalesOrderLine createTestSalesOrderLine(SalesOrder salesOrder, SalesOrderLineRepository salesOrderLineRepository, List<Long> testSalesOrderLineIdList) {
		SalesOrderLine testSalesOrderLine = createTestSalesOrderLine(salesOrder, salesOrderLineRepository);
		testSalesOrderLineIdList.add(testSalesOrderLine.getSalesOrderLineId());
		return testSalesOrderLine;
	}
	
	
	public static Warehouse createTestWarehouse() {
		return Warehouse.builder()
				.warehouseId(null)
				.warehouseName("")
				.warehouseCapacity(0)
				.warehouseLocation(null)
				.build();
	}
	
	public static Warehouse createTestWarehouse(WarehouseRepository warehouseRepository) {
		Warehouse testWarehouse = warehouseRepository.save(createTestWarehouse());
		return testWarehouse;
	}
	
	public static Warehouse createTestWarehouse(WarehouseRepository warehouseRepository, List<Long> testWarehouseIdList) {
		Warehouse testWarehouse = createTestWarehouse(warehouseRepository);
		testWarehouseIdList.add(testWarehouse.getWarehouseId());
		return testWarehouse;
	}
	
}
