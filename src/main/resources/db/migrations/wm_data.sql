--CREATE DATABASE
CREATE DATABASE wm
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE EXTENSION postgis;

-- PARTNER
INSERT INTO partner (partner_name, partner_email, partner_telephone, partner_address)
VALUES ('Alpha Kft', 'alpha@kft.hu', '+361111111', 'Budapest'),
       ('Beta Zrt', 'beta@zrt.hu', '+362222222', 'Debrecen'),
       ('Gamma Bt', 'gamma@bt.hu', '+363333333', 'Szeged'),
       ('Delta Kft', 'delta@kft.hu', '+364444444', 'Pécs'),
       ('Epsilon Kft', 'epsilon@kft.hu', '+365555555', 'Győr'),
       ('Zeta Kft', 'zeta@kft.hu', '+366666666', 'Miskolc'),
       ('Eta Bt', 'eta@bt.hu', '+367777777', 'Nyíregyháza'),
       ('Theta Kft', 'theta@kft.hu', '+368888888', 'Kecskemét'),
       ('Iota Kft', 'iota@kft.hu', '+369999999', 'Székesfehérvár'),
       ('Kappa Zrt', 'kappa@zrt.hu', '+361010101', 'Szombathely');

-- PRODUCT
INSERT INTO product (product_sku, product_name, product_category, product_purchase_price, product_sales_price,
                     product_vat_key)
VALUES ('SKU001', 'Laptop', 'IT', 200000, 250000, 27),
       ('SKU002', 'Egér', 'IT', 2000, 3500, 27),
       ('SKU003', 'Billentyűzet', 'IT', 5000, 8000, 27),
       ('SKU004', 'Monitor', 'IT', 40000, 55000, 27),
       ('SKU005', 'Nyomtató', 'IT', 30000, 42000, 27),
       ('SKU006', 'Papír', 'Iroda', 1000, 1500, 27),
       ('SKU007', 'Toll', 'Iroda', 200, 500, 27),
       ('SKU008', 'Szék', 'Bútor', 15000, 25000, 27),
       ('SKU009', 'Asztal', 'Bútor', 30000, 50000, 27),
       ('SKU010', 'USB', 'IT', 1500, 3000, 27);

-- WAREHOUSE
INSERT INTO warehouse (warehouse_id, warehouse_capacity, warehouse_name, warehouse_location) VALUES
        (1, 1000, 'Raktár 1', ST_GeomFromText('POINT(20.784 47.497)', 4326)),  -- Budapest
        (2, 2000, 'Raktár 2', ST_GeomFromText('POINT(18.233 46.0727)', 4326)), -- Pécs
        (3, 1500, 'Raktár 3', ST_GeomFromText('POINT(19.040 47.497)', 4326)),  -- Debrecen
        (4, 1100, 'Raktár 4', ST_GeomFromText('POINT(21.724 47.955)', 4326)),  -- Nyíregyháza
        (5, 1600, 'Raktár 5', ST_GeomFromText('POINT(19.1011 46.8965)', 4326)),-- Kecskemét
        (6, 1400, 'Raktár 6', ST_GeomFromText('POINT(18.416 47.186)', 4326)),  -- Székesfehérvár
        (7, 2500, 'Raktár 7', ST_GeomFromText('POINT(16.584 47.685)', 4326)),  -- Sopron
        (8, 1200, 'Raktár 8', ST_GeomFromText('POINT(17.633 46.357)', 4326)),  -- Szombathely
        (9, 1800, 'Raktár 9', ST_GeomFromText('POINT(19.808 47.497)', 4326)),  -- Győr
        (10, 1300, 'Raktár 10', ST_GeomFromText('POINT(20.640 46.771)', 4326)); -- Székesfehérvár

-- PURCHASE ORDER
INSERT INTO purchase_order (purchase_order_number, purchase_order_date, purchase_order_status, purchase_order_net_sum,
                            purchase_order_vat_sum, purchase_order_gross_sum, partner_id)
VALUES ('PO001', '2025-01-01', 'OPEN', 100000, 27000, 127000, 1),
       ('PO002', '2025-01-02', 'CLOSED', 200000, 54000, 254000, 2),
       ('PO003', '2025-01-03', 'OPEN', 150000, 40500, 190500, 3),
       ('PO004', '2025-01-04', 'CLOSED', 180000, 48600, 228600, 4),
       ('PO005', '2025-01-05', 'OPEN', 120000, 32400, 152400, 5),
       ('PO006', '2025-01-06', 'CLOSED', 220000, 59400, 279400, 6),
       ('PO007', '2025-01-07', 'OPEN', 130000, 35100, 165100, 7),
       ('PO008', '2025-01-08', 'CLOSED', 170000, 45900, 215900, 8),
       ('PO009', '2025-01-09', 'OPEN', 90000, 24300, 114300, 9),
       ('PO010', '2025-01-10', 'CLOSED', 210000, 56700, 266700, 10);

-- PURCHASE ORDER LINE
INSERT INTO purchase_order_line (purchase_order_id, product_id, purchase_order_line_price, purchase_order_line_vat_key,
                                 purchase_order_line_quantity, purchase_order_line_net_sum, purchase_order_line_vat_sum,
                                 purchase_order_line_gross_sum)
VALUES (1, 1, 200000, 27, 1, 200000, 54000, 254000),
       (1, 2, 2000, 27, 10, 20000, 5400, 25400),
       (2, 3, 5000, 27, 20, 100000, 27000, 127000),
       (2, 4, 40000, 27, 2, 80000, 21600, 101600),
       (3, 5, 30000, 27, 3, 90000, 24300, 114300),
       (3, 6, 1000, 27, 50, 50000, 13500, 63500),
       (4, 7, 200, 27, 100, 20000, 5400, 25400),
       (5, 8, 15000, 27, 5, 75000, 20250, 95250),
       (6, 9, 30000, 27, 4, 120000, 32400, 152400),
       (7, 10, 1500, 27, 30, 45000, 12150, 57150);

-- SALES ORDER
INSERT INTO sales_order (sales_order_number, sales_order_date, sales_order_status, sales_order_net_sum,
                         sales_order_vat_sum, sales_order_gross_sum, partner_id)
VALUES ('SO001', '2025-02-01', 'OPEN', 120000, 32400, 152400, 1),
       ('SO002', '2025-02-02', 'CLOSED', 80000, 21600, 101600, 2),
       ('SO003', '2025-02-03', 'OPEN', 150000, 40500, 190500, 3),
       ('SO004', '2025-02-04', 'CLOSED', 90000, 24300, 114300, 4),
       ('SO005', '2025-02-05', 'OPEN', 200000, 54000, 254000, 5),
       ('SO006', '2025-02-06', 'CLOSED', 170000, 45900, 215900, 6),
       ('SO007', '2025-02-07', 'OPEN', 110000, 29700, 139700, 7),
       ('SO008', '2025-02-08', 'CLOSED', 95000, 25650, 120650, 8),
       ('SO009', '2025-02-09', 'OPEN', 130000, 35100, 165100, 9),
       ('SO010', '2025-02-10', 'CLOSED', 140000, 37800, 177800, 10);

-- SALES ORDER LINE
INSERT INTO sales_order_line (sales_order_id, product_id, sales_order_line_price, sales_order_line_vat_key,
                              sales_order_line_quantity, sales_order_line_net_sum, sales_order_line_vat_sum,
                              sales_order_line_gross_sum)
VALUES (1, 1, 250000, 27, 1, 250000, 67500, 317500),
       (1, 2, 3500, 27, 5, 17500, 4725, 22225),
       (2, 3, 8000, 27, 10, 80000, 21600, 101600),
       (3, 4, 55000, 27, 2, 110000, 29700, 139700),
       (4, 5, 42000, 27, 1, 42000, 11340, 53340),
       (5, 6, 1500, 27, 20, 30000, 8100, 38100),
       (6, 7, 500, 27, 50, 25000, 6750, 31750),
       (7, 8, 25000, 27, 2, 50000, 13500, 63500),
       (8, 9, 50000, 27, 1, 50000, 13500, 63500),
       (9, 10, 3000, 27, 10, 30000, 8100, 38100);

-- INVOICE
INSERT INTO invoice (invoice_number, invoice_date, invoice_status, invoice_net_sum, invoice_vat_sum, invoice_gross_sum,
                     partner_id)
VALUES ('INV001', '2025-03-01', 'PAID', 120000, 32400, 152400, 1),
       ('INV002', '2025-03-02', 'OPEN', 80000, 21600, 101600, 2),
       ('INV003', '2025-03-03', 'PAID', 150000, 40500, 190500, 3),
       ('INV004', '2025-03-04', 'OPEN', 90000, 24300, 114300, 4),
       ('INV005', '2025-03-05', 'PAID', 200000, 54000, 254000, 5),
       ('INV006', '2025-03-06', 'OPEN', 170000, 45900, 215900, 6),
       ('INV007', '2025-03-07', 'PAID', 110000, 29700, 139700, 7),
       ('INV008', '2025-03-08', 'OPEN', 95000, 25650, 120650, 8),
       ('INV009', '2025-03-09', 'PAID', 130000, 35100, 165100, 9),
       ('INV010', '2025-03-10', 'OPEN', 140000, 37800, 177800, 10);

-- INVOICE LINE
INSERT INTO invoice_line (invoice_id, product_id, invoice_line_price, invoice_line_vat_key, invoice_line_quantity,
                          invoice_line_net_sum, invoice_line_vat_sum)
VALUES (1, 1, 250000, 27, 1, 250000, 67500),
       (1, 2, 3500, 27, 2, 7000, 1890),
       (2, 3, 8000, 27, 5, 40000, 10800),
       (3, 4, 55000, 27, 1, 55000, 14850),
       (4, 5, 42000, 27, 2, 84000, 22680),
       (5, 6, 1500, 27, 10, 15000, 4050),
       (6, 7, 500, 27, 20, 10000, 2700),
       (7, 8, 25000, 27, 1, 25000, 6750),
       (8, 9, 50000, 27, 1, 50000, 13500),
       (9, 10, 3000, 27, 5, 15000, 4050);