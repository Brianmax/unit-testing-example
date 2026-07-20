--liquibase formatted sql

--changeset pricing-demo:902-move-tables-to-schemas
ALTER TABLE public.products SET SCHEMA catalog;
ALTER TABLE public.discounts SET SCHEMA catalog;
ALTER TABLE public.orders SET SCHEMA sales;
ALTER TABLE public.order_items SET SCHEMA sales;
ALTER TABLE public.invoices SET SCHEMA billing;
--rollback ALTER TABLE catalog.products SET SCHEMA public;
--rollback ALTER TABLE catalog.discounts SET SCHEMA public;
--rollback ALTER TABLE sales.orders SET SCHEMA public;
--rollback ALTER TABLE sales.order_items SET SCHEMA public;
--rollback ALTER TABLE billing.invoices SET SCHEMA public;
