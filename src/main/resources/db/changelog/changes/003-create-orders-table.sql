--liquibase formatted sql

--changeset pricing-demo:003-create-orders-table
CREATE TABLE orders (
    id UUID NOT NULL PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    discount_id UUID,
    subtotal DECIMAL(12,2),
    discount_amount DECIMAL(12,2),
    total DECIMAL(12,2),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_orders_discount_id FOREIGN KEY (discount_id) REFERENCES discounts (id)
);

CREATE INDEX idx_orders_discount_id ON orders (discount_id);
CREATE INDEX idx_orders_customer_id ON orders (customer_id);
--rollback DROP TABLE orders;
