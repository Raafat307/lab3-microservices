-- الاتصال بقاعدة بيانات الدفعات
\c payments

-- ============================================
-- جدول الدفعات
-- ============================================
CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         payment_uid UUID NOT NULL UNIQUE,
                         status VARCHAR(20) NOT NULL CHECK (status IN ('PAID', 'CANCELED')),
                         price INT NOT NULL
);

-- منح الصلاحيات
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO program;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO program;