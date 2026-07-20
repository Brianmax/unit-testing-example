--liquibase formatted sql

--changeset pricing-demo:900-seed-products context:demo-data
-- Cheat sheet:
--   11111111-1111-1111-1111-111111111111  Widget Standard  19.99
--   22222222-2222-2222-2222-222222222222  Widget Pro       49.99
--   33333333-3333-3333-3333-333333333333  Gadget Mini       9.99
--   44444444-4444-4444-4444-444444444444  Gadget Max      129.99
--   55555555-5555-5555-5555-555555555555  Carrying Case    14.50
INSERT INTO products (id, sku, name, unit_price, created_at, updated_at) VALUES
  ('11111111-1111-1111-1111-111111111111', 'WGT-STD',  'Widget Standard', 19.99,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('22222222-2222-2222-2222-222222222222', 'WGT-PRO',  'Widget Pro',      49.99,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('33333333-3333-3333-3333-333333333333', 'GDG-MINI', 'Gadget Mini',     9.99,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('44444444-4444-4444-4444-444444444444', 'GDG-MAX',  'Gadget Max',      129.99, '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('55555555-5555-5555-5555-555555555555', 'ACC-CASE', 'Carrying Case',   14.50,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z');
--rollback DELETE FROM products WHERE id IN ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555');

--changeset pricing-demo:900-seed-discounts context:demo-data
-- Cheat sheet:
--   aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa  WELCOME10  10% off, active
--   bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb  SAVE20     20% off, min $100, active
--   cccccccc-cccc-cccc-cccc-cccccccccccc  FLAT5      $5 off, min $20, active
--   dddddddd-dddd-dddd-dddd-dddddddddddd  EXPIRED50  50% off, INACTIVE
INSERT INTO discounts (id, code, type, value, min_order_amount, active, created_at, updated_at) VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'WELCOME10', 'PERCENTAGE',   10.0000, NULL,   TRUE,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'SAVE20',    'PERCENTAGE',   20.0000, 100.00, TRUE,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'FLAT5',     'FIXED_AMOUNT', 5.0000,  20.00,  TRUE,  '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z'),
  ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'EXPIRED50', 'PERCENTAGE',   50.0000, NULL,   FALSE, '2026-01-01T09:00:00Z', '2026-01-01T09:00:00Z');
--rollback DELETE FROM discounts WHERE id IN ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'dddddddd-dddd-dddd-dddd-dddddddddddd');

--changeset pricing-demo:900-seed-orders context:demo-data
-- Cheat sheet:
--   66666666-6666-6666-6666-666666666661  CONFIRMED, has an invoice
--   66666666-6666-6666-6666-666666666662  PENDING, ready for POST /confirm
INSERT INTO orders (id, customer_id, status, discount_id, subtotal, discount_amount, total, created_at, updated_at) VALUES
  ('66666666-6666-6666-6666-666666666661', '99999999-9999-9999-9999-999999999991', 'CONFIRMED', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 49.97, 5.00, 44.97, '2026-01-02T10:00:00Z', '2026-01-02T10:00:00Z');
INSERT INTO orders (id, customer_id, status, discount_id, created_at, updated_at) VALUES
  ('66666666-6666-6666-6666-666666666662', '99999999-9999-9999-9999-999999999992', 'PENDING', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '2026-01-03T11:00:00Z', '2026-01-03T11:00:00Z');
--rollback DELETE FROM orders WHERE id IN ('66666666-6666-6666-6666-666666666661', '66666666-6666-6666-6666-666666666662');

--changeset pricing-demo:900-seed-order-items context:demo-data
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price_snapshot, created_at, updated_at) VALUES
  ('77777777-7777-7777-7777-777777777771', '66666666-6666-6666-6666-666666666661', '11111111-1111-1111-1111-111111111111', 2, 19.99,  '2026-01-02T10:00:00Z', '2026-01-02T10:00:00Z'),
  ('77777777-7777-7777-7777-777777777772', '66666666-6666-6666-6666-666666666661', '33333333-3333-3333-3333-333333333333', 1, 9.99,   '2026-01-02T10:00:00Z', '2026-01-02T10:00:00Z'),
  ('77777777-7777-7777-7777-777777777773', '66666666-6666-6666-6666-666666666662', '44444444-4444-4444-4444-444444444444', 1, 129.99, '2026-01-03T11:00:00Z', '2026-01-03T11:00:00Z'),
  ('77777777-7777-7777-7777-777777777774', '66666666-6666-6666-6666-666666666662', '55555555-5555-5555-5555-555555555555', 3, 14.50,  '2026-01-03T11:00:00Z', '2026-01-03T11:00:00Z');
--rollback DELETE FROM order_items WHERE id IN ('77777777-7777-7777-7777-777777777771', '77777777-7777-7777-7777-777777777772', '77777777-7777-7777-7777-777777777773', '77777777-7777-7777-7777-777777777774');

--changeset pricing-demo:900-seed-invoices context:demo-data
-- Cheat sheet:
--   88888888-8888-8888-8888-888888888881  invoice for the confirmed order above
INSERT INTO invoices (id, order_id, subtotal, discount_amount, tax_rate, tax_amount, total, issued_at, created_at, updated_at) VALUES
  ('88888888-8888-8888-8888-888888888881', '66666666-6666-6666-6666-666666666661', 49.97, 5.00, 0.0750, 3.37, 48.34, '2026-01-02T10:05:00Z', '2026-01-02T10:05:00Z', '2026-01-02T10:05:00Z');
--rollback DELETE FROM invoices WHERE id IN ('88888888-8888-8888-8888-888888888881');
