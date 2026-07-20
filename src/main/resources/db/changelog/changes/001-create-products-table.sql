--liquibase formatted sql

--changeset pricing-demo:001-create-products-table
CREATE TABLE products (
    id UUID NOT NULL PRIMARY KEY,
    sku VARCHAR(64) NOT NULL,
    name VARCHAR(200) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_products_sku UNIQUE (sku)
);
--rollback DROP TABLE products;
