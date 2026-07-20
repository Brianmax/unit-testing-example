--liquibase formatted sql

--changeset pricing-demo:901-create-schemas
CREATE SCHEMA IF NOT EXISTS catalog;
CREATE SCHEMA IF NOT EXISTS sales;
CREATE SCHEMA IF NOT EXISTS billing;
--rollback DROP SCHEMA IF EXISTS catalog CASCADE;
--rollback DROP SCHEMA IF EXISTS sales CASCADE;
--rollback DROP SCHEMA IF EXISTS billing CASCADE;
