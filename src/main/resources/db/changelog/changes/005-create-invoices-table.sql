--liquibase formatted sql

--changeset pricing-demo:005-create-invoices-table
CREATE TABLE invoices (
    id UUID NOT NULL PRIMARY KEY,
    order_id UUID NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) NOT NULL,
    tax_rate DECIMAL(6,4) NOT NULL,
    tax_amount DECIMAL(12,2) NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    issued_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_invoices_order_id UNIQUE (order_id),
    CONSTRAINT fk_invoices_order_id FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE INDEX idx_invoices_order_id ON invoices (order_id);
--rollback DROP TABLE invoices;
