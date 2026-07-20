--liquibase formatted sql

--changeset pricing-demo:002-create-discounts-table
CREATE TABLE discounts (
    id UUID NOT NULL PRIMARY KEY,
    code VARCHAR(64) NOT NULL,
    type VARCHAR(20) NOT NULL,
    value DECIMAL(12,4) NOT NULL,
    min_order_amount DECIMAL(12,2),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_discounts_code UNIQUE (code)
);
--rollback DROP TABLE discounts;
